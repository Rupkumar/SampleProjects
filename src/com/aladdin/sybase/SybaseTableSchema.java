package com.aladdin.sybase;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SybaseTableSchema
{
	private final String tableName;

	private final Map<String, SybaseTableColumn> colMap;

	private SybaseTableIndexInfo key;

	private Map<String, SybaseTableIndexInfo> indexes = new LinkedHashMap<>();

	SybaseTableSchema(String tableName, Map<String, SybaseTableColumn> colMap)
	{
		this.tableName = tableName;
		this.colMap = colMap;
	}

	public SybaseTableIndexInfo getKey()
	{
		return this.key;
	}

	public void setKey(SybaseTableIndexInfo key)
	{
		this.key = key;
	}

	public Map<String, SybaseTableIndexInfo> getIndexes()
	{
		return this.indexes;
	}

	public void setIndexes(Map<String, SybaseTableIndexInfo> indexes)
	{
		this.indexes = indexes;
	}

	public Map<String, SybaseTableColumn> getColMap()
	{
		return this.colMap;
	}

	public String getTableName()
	{
		return this.tableName;
	}
}