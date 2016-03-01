package com.aladdin.entities;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.cache.configuration.Factory;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.util.ReflectionUtils;

import com.aladdin.database.DatabaseHelper;
import com.aladdin.entities.annotations.AladdinColumn;
import com.aladdin.entities.annotations.AladdinDatabaseObject;
import com.bfm.util.BFMUtil;

public class ReflectionAladdinSybaseCacheStoreAdapter<K extends Serializable, V extends SybaseTable<K>> extends AbstractAladdinSybaseCacheStoreAdapter<K, V>
{
	private static final long serialVersionUID = -5634302517934267524L;

	public ReflectionAladdinSybaseCacheStoreAdapter(Class<V> clazz, DatabaseHelper DB)
	{
		super(clazz, DB);
	}

	public ReflectionAladdinSybaseCacheStoreAdapter()
	{
		super();
	}

	@Override
	protected QueriesImpl buildQueries(Class<V> clazz) throws SecurityException
	{
		if(clazz == null)
			throw new IllegalArgumentException("Clazz cannot be null!");
		else if(!clazz.isAnnotationPresent(AladdinDatabaseObject.class))
			throw new IllegalArgumentException("Specified class does not posses the AladdinDatabaseObject annotation.");

		AladdinDatabaseObject dbObjAnnotation = clazz.getAnnotation(AladdinDatabaseObject.class);
		StringBuilder andEquals = new StringBuilder();
		StringBuilder commaEquals = new StringBuilder();
		StringBuilder columnList = new StringBuilder();
		StringBuilder line = new StringBuilder(40);
		StringBuilder qMarks = new StringBuilder();
		List<Pair<ColumnType, Field>> pkColTypes = new LinkedList<>();
		List<Pair<ColumnType, Field>> colTypes = new LinkedList<>();
		ReflectionUtils.doWithFields(clazz, (field)->
		{
			if(!field.isAccessible())
				field.setAccessible(true);

			AladdinColumn columnInfo = field.getAnnotation(AladdinColumn.class);
			QuerySqlField igniteInfo = field.getAnnotation(QuerySqlField.class);
			String columnName = columnInfo.columnName();

			if(columnName.isEmpty())
			{
				String fieldName = field.getName();

				if(igniteInfo == null)
					columnName = fieldName;
				else
				{
					String queryName = igniteInfo.name();

					if(queryName.isEmpty())
						columnName = fieldName;
					else
						columnName = queryName;
				}
			}

			if(commaEquals.length() > 0)
			{
				commaEquals.append(',');
				columnList.append(',');
				qMarks.append(',');
			}

			line.setLength(0);
			line.append("t.").append(columnName).append("=?");
			String str = line.toString();
			commaEquals.append(str);
			columnList.append(columnName);
			qMarks.append('?');
			ColumnType columnType = columnInfo.columnType();

			if(columnType == ColumnType.AUTO)
				columnType = ColumnType.getTypeFromClass(field.getType());

			ImmutablePair<ColumnType, Field> pair = new ImmutablePair<>(columnType, field);
			colTypes.add(pair);

			if(columnInfo.primaryKey())
			{
				// if(!Modifier.isFinal(field.getModifiers()))
				// throw new RuntimeException("Primary key fields must be final as they cannot be changed!"); NEEDED NO FINAL FOR EXTERNALIZABLE
				if(andEquals.length() > 0)
					andEquals.append(" AND ");

				andEquals.append(str);
				pkColTypes.add(pair);
			}
		}, (field)->field.isAnnotationPresent(AladdinColumn.class));

		Class<?>[] constructorClasses = new Class<?>[colTypes.size()];
		int i = 0;

		for(Pair<ColumnType, Field> pair : colTypes)
			constructorClasses[i++] = pair.getRight().getType();

		Constructor<V> constructor;

		try
		{
			constructor = clazz.getConstructor(constructorClasses);
		}
		catch(NoSuchMethodException e)
		{
			throw new RuntimeException("A constructor must be declared with all columns in the exact order they appear!", e);
		}

		String fqn = BFMUtil.getTbl(dbObjAnnotation.getTbl());
		return new QueriesImpl("MERGE INTO " + fqn + " t USING (SELECT 1) AS v ON " + andEquals + " WHEN NOT MATCHED THEN INSERT (" + columnList + ") VALUES ("
				+ qMarks + ") WHEN MATCHED THEN UPDATE SET " + commaEquals, "SELECT * FROM " + fqn + " WHERE " + commaEquals, constructor, pkColTypes, colTypes);
	}

	@Override
	protected V loadFromResultSet(ResultSet result) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException
	{
		Object[] args = new Object[this.queries.getColTypes().size()];
		int i = 0;

		for(Pair<ColumnType, Field> pair : this.queries.getColTypes())
		{
			if(i == 7)
				pair.toString();
			Field right = pair.getRight();
			String columnName = right.getAnnotation(AladdinColumn.class).columnName();

			if(columnName.isEmpty())
			{
				QuerySqlField igniteInfo = right.getAnnotation(QuerySqlField.class);
				String fieldName = right.getName();

				if(igniteInfo == null)
					columnName = fieldName;
				else
				{
					String igniteName = igniteInfo.name();

					if(igniteName.isEmpty())
						columnName = fieldName;
					else
						columnName = igniteName;
				}
			}

			args[i++] = pair.getLeft().getValue(result, columnName);
		}

		return this.queries.getLoadAllConstructor().newInstance(args);
	}

	@Override
	protected void setInsertValues(PreparedStatement stmt, K key, V entry) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		int i = 1;

		for(Pair<ColumnType, Field> pair : this.queries.getPkColPairs())
			pair.getLeft().setValue(stmt, i++, pair.getRight().get(entry));

		int j = i + this.queries.getColTypes().size();

		for(Pair<ColumnType, Field> pair : this.queries.getColTypes())
		{
			ColumnType columnType = pair.getLeft();
			Object obj = pair.getRight().get(entry);
			columnType.setValue(stmt, i++, obj);
			columnType.setValue(stmt, j++, obj);
		}
	}

	@Override
	protected void setLoadValues(PreparedStatement stmt, K key, V entry) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		int i = 1;

		for(Pair<ColumnType, Field> pair : this.queries.getPkColPairs())
			pair.getLeft().setValue(stmt, i++, pair.getRight().get(entry));
	}

	@Override
	public FactoryImpl getFactory()
	{
		return new FactoryImpl();
	}

	private class FactoryImpl implements Factory<ReflectionAladdinSybaseCacheStoreAdapter<K, V>>, Serializable
	{
		private static final long serialVersionUID = 3665941330299704601L;

		public FactoryImpl()
		{
		} // NEED THIS FOR SERIALIZATION!

		@Override
		public ReflectionAladdinSybaseCacheStoreAdapter<K, V> create()
		{
			return ReflectionAladdinSybaseCacheStoreAdapter.this;
		}
	}

	private class QueriesImpl implements Queries<K, V>
	{
		private static final long serialVersionUID = -9074552594917587962L;

		private final String mergeQuery;

		private final String loadQuery;

		private final Constructor<V> loadAllConstructor;

		private final List<Pair<ColumnType, Field>> pkColPairs;

		private final List<Pair<ColumnType, Field>> colTypes;

		private QueriesImpl(String mergeQuery, String loadQuery, Constructor<V> loadAllConstructor, List<Pair<ColumnType, Field>> pkColTypes,
				List<Pair<ColumnType, Field>> colTypes)
		{
			this.loadAllConstructor = loadAllConstructor;
			this.loadQuery = loadQuery;
			this.mergeQuery = mergeQuery;
			this.pkColPairs = pkColTypes;
			this.colTypes = colTypes;
		}

		@Override
		public String getMergeQuery()
		{
			return this.mergeQuery;
		}

		@Override
		public String getLoadQuery()
		{
			return this.loadQuery;
		}

		@Override
		public Constructor<V> getLoadAllConstructor()
		{
			return this.loadAllConstructor;
		}

		@Override
		public List<Pair<ColumnType, Field>> getPkColPairs()
		{
			return this.pkColPairs;
		}

		@Override
		public List<Pair<ColumnType, Field>> getColTypes()
		{
			return this.colTypes;
		}
	}
}