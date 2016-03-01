package com.aladdin.entities;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aladdin.cloud.AbstractAladdinCloud;

public class PoCCacheStoreAdapter<K extends Serializable, V extends SybaseTable<K>> extends AbstractAladdinSybaseCacheStoreAdapter<K, V>
{
	private final static Logger log = LogManager.getLogger(PoCCacheStoreAdapter.class);

	public PoCCacheStoreAdapter()
	{
	}

	@Override
	public V load(K key) throws CacheLoaderException
	{
		throw new CacheLoaderException("Cannot load individual keys from file!");
	}

	@Override
	public void delete(Object arg0) throws CacheWriterException
	{
		throw new CacheWriterException("Cannot delete keys from file!");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCache(IgniteBiInClosure<K, V> clo, Object... args)
	{
		long nodeId = (long) args[1];

		if(nodeId >= 0 && nodeId != AbstractAladdinCloud.getSingletonNodeId())
		{
			log.info("This cache is being fully loaded from another node! Not loading from this one.");
			return;
		}

		log.info("Loading cache...");

		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream((File) args[0])))
		{
			int i = 0;

			while(true)
			{
				V obj = (V) in.readObject();
				clo.apply(obj.getKey(), obj);

				if(++i % 100000 == 0)
					log.info("Inserted: " + i);
			}
		}
		catch(EOFException e)
		{}
		catch(IOException | ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		log.info("Cache loaded...");
	}

	@Override
	protected com.aladdin.entities.AbstractAladdinSybaseCacheStoreAdapter.Queries<K, V> buildQueries(Class<V> clazz) throws SecurityException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected V loadFromResultSet(ResultSet result) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setInsertValues(PreparedStatement stmt, K key, V entry) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void setLoadValues(PreparedStatement stmt, K key, V entry) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		// TODO Auto-generated method stub

	}
}