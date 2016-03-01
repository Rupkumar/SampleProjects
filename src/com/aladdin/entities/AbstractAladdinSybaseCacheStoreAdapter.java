package com.aladdin.entities;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.cache.Cache.Entry;
import javax.cache.configuration.Factory;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;

import com.aladdin.database.AladdinConnection;
import com.aladdin.database.DatabaseHelper;

public abstract class AbstractAladdinSybaseCacheStoreAdapter<K extends Serializable, V extends SybaseTable<K>> extends CacheStoreAdapter<K, V> implements
		Externalizable
{
	private static final long serialVersionUID = 2349266154224742703L;

	private final static int MAX_BATCH_SIZE = 10000;

	private Class<V> clazz;

	protected Queries<K, V> queries;

	private DatabaseHelper DB;

	public AbstractAladdinSybaseCacheStoreAdapter(Class<V> clazz, DatabaseHelper DB)
	{
		this.clazz = clazz;
		this.queries = this.buildQueries(clazz);
		this.DB = DB;
	}

	public AbstractAladdinSybaseCacheStoreAdapter()
	{
	}

	protected interface Queries<K extends Serializable, V extends SybaseTable<K>> extends Serializable
	{
		String getMergeQuery();

		String getLoadQuery();

		Constructor<V> getLoadAllConstructor();

		List<Pair<ColumnType, Field>> getPkColPairs();

		List<Pair<ColumnType, Field>> getColTypes();
	}

	protected abstract Queries<K, V> buildQueries(Class<V> clazz) throws SecurityException;

	protected abstract V loadFromResultSet(ResultSet result) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException;

	protected abstract void setInsertValues(PreparedStatement stmt, K key, V entry) throws IllegalArgumentException, IllegalAccessException, SQLException;

	protected abstract void setLoadValues(PreparedStatement stmt, K key, V entry) throws IllegalArgumentException, IllegalAccessException, SQLException;

	public Factory<? extends AbstractAladdinSybaseCacheStoreAdapter<K, V>> getFactory()
	{
		return new FactoryImpl<AbstractAladdinSybaseCacheStoreAdapter<K, V>>();
	}

	private class FactoryImpl<A extends AbstractAladdinSybaseCacheStoreAdapter<K, V>> implements Factory<AbstractAladdinSybaseCacheStoreAdapter<K, V>>,
			Serializable
	{
		private static final long serialVersionUID = 3665941330299704601L;

		@Override
		public AbstractAladdinSybaseCacheStoreAdapter<K, V> create()
		{
			return AbstractAladdinSybaseCacheStoreAdapter.this;
		}
	}

	@Override
	public V load(K key) throws CacheLoaderException
	{
		return this.doPrepStatementTask((stmt)->
		{
			try
			{
				return this.loadFromResultSet(stmt.executeQuery());
			}
			catch(Exception e)
			{
				throw new CacheLoaderException("Failed to load key: " + key, e);
			}
		}, this.queries.getLoadQuery(), false);
	}

	@Override
	public final Map<K, V> loadAll(Iterable<? extends K> keys)
	{
		return super.loadAll(keys);
	}

	@Override
	public void write(Entry<? extends K, ? extends V> entry) throws CacheWriterException
	{
		this.doPrepStatementTask((stmt)->
		{
			this.setInsertValues(stmt, entry.getKey(), entry.getValue());
			stmt.executeUpdate();
			return null;
		}, this.queries.getMergeQuery(), false);
	}

	@FunctionalInterface
	private interface PrepStmtTask<O>
	{
		O doTask(PreparedStatement stmt) throws Exception;
	}

	private <O> O doPrepStatementTask(PrepStmtTask<O> task, String query, boolean getGPX)
	{
		try(AladdinConnection conn = getGPX ? this.DB.getGPXConnection() : this.DB.getWriteConnection())
		{
			try(PreparedStatement stmt = conn.preparedStatement(query))
			{
				return task.doTask(stmt);
			}
		}
		catch(Exception e)
		{
			throw new CacheWriterException("Failed to do prepared statement!", e);
		}
	}

	@Override
	public void writeAll(Collection<Entry<? extends K, ? extends V>> entries)
	{
		this.doPrepStatementTask((stmt)->
		{
			int currBatch = 0;

			for(Entry<? extends K, ? extends V> entry : entries)
			{
				this.setInsertValues(stmt, entry.getKey(), entry.getValue());

				if(++currBatch == MAX_BATCH_SIZE)
				{
					stmt.executeBatch();
					currBatch = 0;
				}
			}

			if(currBatch > 0)
				stmt.executeBatch();

			return null;
		}, this.queries.getMergeQuery(), false);
	}

	@Override
	public void delete(Object arg0) throws CacheWriterException
	{
		throw new NotImplementedException("STOP BEING LAZY AND DO THIS");
	}

	@Override
	public final void deleteAll(Collection<?> arg0)
	{
		throw new NotImplementedException("STOP BEING LAZY AND DO THIS");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCache(IgniteBiInClosure<K, V> clo, Object... args)
	{
		Object[] temp = args.clone();
		boolean getGPX = false;

		if((temp.length > 1) && (temp[0] instanceof Boolean))
		{
			Object[] temp2 = new Object[temp.length - 1];

			for(int i = 1; i < temp.length; i++)
				temp2[i - 1] = temp[i];

			getGPX = (boolean) temp[0];
			temp = temp2;
		}

		Object[] finalArgs = temp;

		this.doPrepStatementTask((stmt)->
		{
			for(int i = 1; i < finalArgs.length; i++)
			{
				Pair<ColumnType, Object> pair = (Pair<ColumnType, Object>) finalArgs[i];
				pair.getLeft().setValue(stmt, i, pair.getRight());
			}

			ResultSet results = stmt.executeQuery();
			long ct = 0;

			while(results.next())
			{
				V value = this.loadFromResultSet(results);
				clo.apply(value.getKey(), value);

				if((ct++ % 100000) == 0)
					System.gc();
			}

			return null;
		}, finalArgs[0].toString(), getGPX);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(this.clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.clazz = (Class<V>) in.readObject();
		this.queries = this.buildQueries(this.clazz);
		this.DB = DatabaseHelper.getInstance();
	}
}