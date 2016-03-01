package com.aladdin.sybase;

import java.math.BigInteger;

public final class SybaseTableColumn
{
	public final SybaseTableSchema table;

	public final String columnName;

	public final SybaseType sybaseType;

	public final int length;

	public final boolean nullable;

	SybaseTableColumn(SybaseTableSchema table, String columnName, int columnType, short status, int length)
	{
		this.sybaseType = SybaseType.getFromCode(columnType);
		this.table = table;
		this.columnName = columnName;
		this.nullable = BigInteger.valueOf(status).testBit(3);
		this.length = length;
	}
}