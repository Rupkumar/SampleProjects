package com.aladdin.sybase;

import java.util.HashMap;
import java.util.Map;

import com.aladdin.entities.ColumnType;
import com.aladdin.entities.annotations.UnmappedSybaseType;

public enum SybaseType
{
	SYBASE_VARBINARY_TYPE(37),
	SYBASE_IMAGE_TYPE(34),
	SYBASE_TINYINT_TYPE(48),
	SYBASE_BIT_TYPE(50),
	SYBASE_SMALLDATETIME_TYPE(58),
	SYBASE_TIME_TYPE(147),
	SYBASE_SHORT_TYPE(52),
	SYBASE_NUMERIC_TYPE2(63),
	SYBASE_TEXT_TYPE(35),
	SYBASE_FLOAT_TYPE2(109),
	SYBASE_FLOAT_TYPE(62),
	SYBASE_INT_TYPE2(38),
	SYBASE_NUMERIC_TYPE(108),
	SYBASE_DECIMAL_TYPE(106),
	SYBASE_DATE_TYPE(123),
	SYBASE_DATETIME_TYPE(61),
	SYBASE_DATETIME_TYPE2(111),
	SYBASE_CHAR_TYPE(47),
	SYBASE_BIANRY_TYPE(45),
	SYBASE_VARCHAR_TYPE(39),
	SYBASE_INT_TYPE(56);

	private final int sybaseCode;

	private SybaseType(int sybaseCode)
	{
		this.sybaseCode = sybaseCode;
	}

	public int getSybaseCode()
	{
		return this.sybaseCode;
	}

	public ColumnType toColumnType(int length)
	{
		return ColumnType.getColumnTypeFromSybaseSchema(this, length);
	}

	private final static Map<Integer, SybaseType> codeMap;

	static
	{
		codeMap = new HashMap<>();

		for(SybaseType type : values())
			codeMap.put(type.sybaseCode, type);
	}

	public static com.aladdin.sybase.SybaseType getFromCode(int columnType)
	{
		SybaseType sybaseType = codeMap.get(columnType);

		if(sybaseType == null)
			throw new UnmappedSybaseType(columnType);

		return sybaseType;
	}
}