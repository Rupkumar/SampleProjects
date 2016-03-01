package com.aladdin.entities;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.aladdin.sybase.SybaseTableColumn;
import com.aladdin.sybase.SybaseTableIndexInfo;
import com.aladdin.sybase.SybaseType;

public enum ColumnType
{
	BOOLEAN("boolean", "Boolean", ColumnTypeComparator.BOOLEAN, "writeBoolean", "readBoolean", true),
	// CHAR("char", "Character", ColumnTypeComparator.CHAR, "writeChar", "readChar", true), NOT SUPPORTED FOR INDEXING...
	BYTE("byte", "Byte", ColumnTypeComparator.BYTE, "writeByte", "readByte", true),
	SHORT("short", "Short", ColumnTypeComparator.SHORT, "writeShort", "readShort", true),
	INT("int", "Integer", ColumnTypeComparator.INT, "writeInt", "readInt", true),
	STRING("String", ColumnTypeComparator.STRING, "writeUTF", "readUTF", false),
	BLOB("byte[]", ColumnTypeComparator.BLOB, "writeObject", "readObject", false),
	DATE("java.sql.Date", ColumnTypeComparator.DATE, "writeObject", "readObject", false),
	DECIMAL("java.math.BigDecimal", ColumnTypeComparator.DECIMAL, "writeObject", "readObject", false),
	DOUBLE("double", "Double", ColumnTypeComparator.DOUBLE, "writeDouble", "readDouble", true),
	AUTO(null, null, null, null, false);

	private final String nonNulljavaType;

	private final String nullJavaType;

	private final Comparator<Object> comparator;

	private final boolean handleNull;

	private final String outStr;

	private final String inStr;

	private final boolean cast;

	private final boolean isPrimitive;

	private ColumnType(String javaType, Comparator<Object> comparator, String outStr, String inStr, boolean isPrimitive)
	{
		this(javaType, comparator, true, outStr, inStr, isPrimitive);
	}

	private ColumnType(String javaType, Comparator<Object> comparator, boolean handleNull, String outStr, String inStr, boolean isPrimitive)
	{
		this(javaType, javaType, comparator, handleNull, outStr, inStr, isPrimitive);
	}

	private ColumnType(String nonNullJavaType, String nullJavaType, Comparator<Object> comparator, String outStr, String inStr, boolean isPrimitive)
	{
		this(nonNullJavaType, nullJavaType, comparator, true, outStr, inStr, isPrimitive);
	}

	private ColumnType(String nonNullJavaType, String nullJavaType, Comparator<Object> comparator, boolean handleNull, String outStr, String inStr,
			boolean isPrimitive)
	{
		this.outStr = outStr;
		this.inStr = inStr;
		this.nonNulljavaType = nonNullJavaType;
		this.nullJavaType = nullJavaType;
		this.comparator = comparator;
		this.handleNull = handleNull;
		this.cast = (inStr != null) && inStr.equals("readObject");
		this.isPrimitive = isPrimitive;
	}

	private String[] getJavaDefinition(SybaseTableColumn column, boolean isPartOfKey, boolean annotate, boolean isAffinity)
	{
		return this.getJavaDefinitionString(column, isPartOfKey, annotate, null, isAffinity);
	}

	private String[] getJavaDefinitionString(SybaseTableColumn column, boolean isPartOfKey, boolean annotate, Integer length, boolean isAffinity)
	{
		String javaType = column.nullable ? this.nullJavaType : this.nonNulljavaType;
		String importStr;

		if(javaType.contains("."))
		{
			importStr = javaType;
			String[] split = javaType.split("\\.");
			javaType = split[split.length - 1];
		}
		else
			importStr = null;

		String fieldName = convertSybaseColNameToJavaFieldName(column.columnName);
		String methodName = convertSybaseColNameToJavaMethodName(column.columnName);
		StringBuilder sb = new StringBuilder();
		StringBuilder fieldDef = new StringBuilder();

		if(annotate)
		{
			int i = 0;
			boolean yep = false;
			boolean is1 = false;
			boolean isSingleColumnIndex = false;

			SybaseTableIndexInfo tableKey = column.table.getKey();

			for(; i < tableKey.keys.size(); i++)
				if(tableKey.keys.get(i).equals(column.columnName))
				{
					yep = true;
					is1 = tableKey.keys.size() == 1;
					break;
				}

			if(yep && !is1)
				sb.append("@QuerySqlField.Group(name=\"").append(tableKey.index_name).append("\", order=").append(i).append(')');
			else if(is1)
				isSingleColumnIndex = true;

			for(SybaseTableIndexInfo index : column.table.getIndexes().values())
			{
				yep = false;
				i = 0;

				for(; i < index.keys.size(); i++)
					if(index.keys.get(i).equals(column.columnName))
					{
						yep = true;
						is1 = index.keys.size() == 1;
						break;
					}

				if(yep && !is1)
					sb.append(sb.length() == 0 ? "" : ", ").append("@QuerySqlField.Group(name=\"").append(index.index_name).append("\", order=").append(i)
							.append(')');
				else if(is1)
					isSingleColumnIndex = true;
			}

			fieldDef.append("@AladdinColumn(columnName=\"").append(column.columnName).append("\", columnType=ColumnType.").append(this.name())
					.append(isPartOfKey ? ", primaryKey=true" : "").append(length == null ? "" : ", length=" + length).append(")\n")
					.append("@QuerySqlField(name=\"").append(column.columnName).append("\"");

			if(isSingleColumnIndex)
				fieldDef.append(", index=true");

			if(sb.length() > 0)
				fieldDef.append(sb.length() == 0 ? "" : ", orderedGroups={").append(sb)
						.append(sb.length() == 0 ? "" : "}");

			fieldDef.append(")\n");
		}
		else if(isAffinity)
			fieldDef.append("@AffinityKeyMapped\n");

		StringBuilder eqIf = new StringBuilder();

		if(column.nullable) // DO A NULL CHECK IF IT CAN BE NULL
			eqIf.append("if(this.").append(fieldName)
					.append(" == null)\n\t{\n\t\tif(o.").append(fieldName)
					.append(" == null)\n\t\t\treturn false;\n\t}\n\telse if(!this.")
					.append(fieldName);
		else
			eqIf.append("if(").append(this.isPrimitive ? "" : "!")
					.append("this.").append(fieldName);

		if(this.isPrimitive && !column.nullable)
			eqIf.append(" != o.").append(fieldName).append(")\n\t\treturn false;");
		else
			eqIf.append(".equals(o.").append(fieldName).append("))\n\t\treturn false;");

		boolean isNullString = (this == STRING) && column.nullable;
		return new String[]
		{
				importStr, // if it's not a standard java primitive, then what do we need to import
				fieldDef.append("private ").append(javaType).append(' ')
						.append(fieldName).append(';').toString(), // build the field definition
				new StringBuilder().append(javaType)
						.append(' ').append(fieldName).toString(), // build the constructor definition
				new StringBuilder().append('\t').append("this.")
						.append(fieldName).append(" = ")
						.append(fieldName).append(';').toString(), // build the constructor body
				new StringBuilder().append("public ").append(javaType).append(" get")
						.append(methodName).append("()\n{\n\treturn this.")
						.append(fieldName).append(";\n}").toString(), // build the getter
				new StringBuilder().append("\tout.").append(isNullString ? "writeObject" : this.outStr).append("(this.").append(fieldName)
						.append(");").toString(), // build the externalizable out writer
				new StringBuilder().append("\tthis.").append(fieldName).append(" = ")
						.append(this.cast || isNullString ? '(' + javaType + ')' : "").append("in.")
						.append(isNullString ? "readObject" : this.inStr).append("();").toString(), // build the externalizable in reader
				new StringBuilder().append("\thcb.append(this.").append(fieldName)
						.append(");").toString(), // build the hash code builder
				eqIf.toString() // build the equals if statement
		};
	}

	public int compare(Object obj1, Object obj2)
	{
		if(this.handleNull)
			if((obj1 == null) && (obj2 == null))
				return 0;
			else if(obj1 == null)
				return -1;
			else if(obj2 == null)
				return 1;

		return this.comparator.compare(obj1, obj2);
	}

	/*
	 * private String buildSetter(String methodName, String fieldName, String javaType)
	 * {
	 * return new StringBuilder().append("public void set").append(methodName).append('(')
	 * .append(javaType).append(' ').append(fieldName).append(")\n{\n\tthis.")
	 * .append(fieldName).append(" = ").append(fieldName).append(";\n}").toString();
	 * }
	 */

	public String getJavaClassString(boolean nullable)
	{
		String val;

		if(this.isPrimitive())
			nullable = true;

		if(nullable)
			val = this.nullJavaType;
		else
			val = this.nonNulljavaType;

		String[] split = val.split("\\.");
		return split[split.length - 1];
	}

	private boolean isPrimitive()
	{
		return (this == DOUBLE) || (this == INT) || (this == SHORT) || (this == BYTE) || (this == BOOLEAN); // || this == CHAR;
	}

	public static String convertSybaseColNameToJavaMethodName(String sybaseColName)
	{
		StringBuilder sb = new StringBuilder();

		for(String str : sybaseColName.split("_"))
			sb.append(StringUtils.capitalize(str));

		String methodName = sb.toString();

		if(methodName.equals("Class")) // cannot create a getter named getClass, it collides with the java.lang.Object.getClass method
			methodName = "ClassVal";

		return methodName;
	}

	public static String convertSybaseColNameToJavaFieldName(String sybaseColName)
	{
		StringBuilder sb = new StringBuilder();

		for(String str : sybaseColName.split("_"))
			if(sb.length() == 0)
				sb.append(str);
			else
				sb.append(StringUtils.capitalize(str));

		String fieldName = sb.toString();

		if(fieldName.equals("package")) // cannot create field with name package
			fieldName = "pkg";
		else if(fieldName.equals("class")) // cannot create field with name class
			fieldName = "clazz";
		else if(fieldName.equals("private"))
			fieldName = "prvt";

		return fieldName;
	}

	public void setValue(PreparedStatement stmt, int index, Object obj) throws SQLException
	{
		if(obj == null)
			stmt.setNull(index, this.getSqlType());
		/*
		 * else if(this == CHAR)
		 * stmt.setString(index, ((Character) obj).toString());
		 */
		else if(this == INT)
			stmt.setInt(index, (int) obj);
		else if(this == DOUBLE)
			stmt.setDouble(index, (double) obj);
		else if(this == STRING)
			stmt.setString(index, obj.toString());
		else if(this == DATE)
		{
			if(obj instanceof Date)
				stmt.setDate(index, (Date) obj);
			else
				stmt.setDate(index, new Date(((java.util.Date) obj).getTime()));
		}
		else if(this == DECIMAL)
			stmt.setBigDecimal(index, (BigDecimal) obj);
		else if(this == BLOB)
		{
			if(obj instanceof Blob)
				stmt.setBlob(index, (Blob) obj);
			else
				stmt.setBlob(index, (InputStream) obj);
		}
		else if(this == SHORT)
			stmt.setShort(index, (short) obj);
		else if(this == BYTE)
			stmt.setByte(index, (byte) obj);
		else if(this == BOOLEAN)
			stmt.setBoolean(index, (boolean) obj);
		else
			throw new RuntimeException(this.name() + " has not yet been setup in setValue(ResultSet, int, Object)!");
	}

	@SuppressWarnings("unchecked")
	public <O> O getValue(ResultSet results, int index) throws SQLException
	{
		Object ret;

		/*
		 * if(this == CHAR)
		 * {
		 * String strVal = results.getString(index);
		 * ret = strVal == null ? null : strVal.charAt(0);
		 * }
		 * else
		 */if(this == INT)
			ret = results.getInt(index);
		else if(this == DOUBLE)
			ret = results.getDouble(index);
		else if(this == STRING)
			ret = results.getString(index);
		else if(this == DATE)
			ret = results.getDate(index);
		else if(this == DECIMAL)
			ret = results.getBigDecimal(index);
		else if(this == BLOB)
			ret = results.getBytes(index);
		else if(this == SHORT)
			ret = results.getShort(index);
		else if(this == BYTE)
			ret = results.getByte(index);
		else if(this == BOOLEAN)
			ret = results.getBoolean(index);
		else
			throw new RuntimeException(this.name() + " has not yet been setup in getValue(ResultSet, int)!");

		return (O) ret;
	}

	@SuppressWarnings("unchecked")
	public <O> O getValue(ResultSet results, String columnName) throws SQLException
	{
		Object ret;

		/*
		 * if(this == CHAR)
		 * {
		 * String strVal = results.getString(columnName);
		 * ret = strVal == null ? null : strVal.charAt(0);
		 * }
		 * else
		 */if(this == INT)
			ret = results.getInt(columnName);
		else if(this == DOUBLE)
			ret = results.getDouble(columnName);
		else if(this == STRING)
			ret = results.getString(columnName);
		else if(this == DATE)
			ret = results.getDate(columnName);
		else if(this == DECIMAL)
			ret = results.getBigDecimal(columnName);
		else if(this == BLOB)
			ret = results.getBytes(columnName);
		else if(this == SHORT)
			ret = results.getShort(columnName);
		else if(this == BYTE)
			ret = results.getByte(columnName);
		else if(this == BOOLEAN)
			ret = results.getBoolean(columnName);
		else
			throw new RuntimeException(this.name() + " has not yet been setup in getValue(ResultSet, String)!");

		return (O) ret;
	}

	private int getSqlType()
	{
		if(this == INT)
			return Types.INTEGER;
		else if(this == STRING)
			return Types.VARCHAR;
		else if(this == DATE)
			return Types.DATE;
		else if(this == DECIMAL)
			return Types.DECIMAL;
		else if(this == DOUBLE)
			return Types.FLOAT;
		else if(this == BLOB)
			return Types.BLOB;
		else if(this == SHORT)
			return Types.SMALLINT;
		else if(this == BYTE)
			return Types.TINYINT;
		else if(this == BOOLEAN)
			return Types.BIT;

		throw new RuntimeException(this.name() + " has not yet been setup in getSqlType()!");
	}

	public static ColumnType getTypeFromClass(Class<?> fieldType)
	{
		/*
		 * if(fieldType.equals(Character.class) || fieldType.equals(char.class))
		 * return CHAR;
		 * else
		 */if(fieldType.equals(Integer.class) || fieldType.equals(int.class))
			return INT;
		else if(fieldType.equals(Double.class) || fieldType.equals(double.class))
			return DOUBLE;
		else if(fieldType.equals(Date.class) || fieldType.equals(java.util.Date.class))
			return DATE;
		else if(fieldType.equals(Short.class) || fieldType.equals(short.class))
			return SHORT;
		else if(fieldType.equals(Byte.class) || fieldType.equals(byte.class))
			return BYTE;
		else if(fieldType.equals(Boolean.class) || fieldType.equals(byte.class))
			return BOOLEAN;
		else if(fieldType.equals(String.class))
			return STRING;
		else if(fieldType.equals(BigDecimal.class))
			return DECIMAL;

		return BLOB;
	}

	public static ColumnType getColumnTypeFromSybaseSchema(SybaseType sybaseType, int length)
	{
		if((sybaseType == SybaseType.SYBASE_INT_TYPE) || (sybaseType == SybaseType.SYBASE_INT_TYPE2))
			return INT;
		else if((sybaseType == SybaseType.SYBASE_FLOAT_TYPE) || (sybaseType == SybaseType.SYBASE_FLOAT_TYPE2))
			return DOUBLE;
		else if(sybaseType == SybaseType.SYBASE_TEXT_TYPE)
			return STRING;
		else if((sybaseType == SybaseType.SYBASE_VARCHAR_TYPE) || (sybaseType == SybaseType.SYBASE_CHAR_TYPE))
			return STRING;
		/*
		 * else if(sybaseType == SybaseType.SYBASE_CHAR_TYPE)
		 * return CHAR;
		 */
		else if((sybaseType == SybaseType.SYBASE_DATETIME_TYPE) || (sybaseType == SybaseType.SYBASE_DATETIME_TYPE2)
				|| (sybaseType == SybaseType.SYBASE_TIME_TYPE)
				|| (sybaseType == SybaseType.SYBASE_SMALLDATETIME_TYPE) || (sybaseType == SybaseType.SYBASE_DATE_TYPE))
			return DATE;
		else if((sybaseType == SybaseType.SYBASE_NUMERIC_TYPE) || (sybaseType == SybaseType.SYBASE_NUMERIC_TYPE2)
				|| (sybaseType == SybaseType.SYBASE_DECIMAL_TYPE))
			return DECIMAL;
		else if(sybaseType == SybaseType.SYBASE_SHORT_TYPE)
			return SHORT;
		else if(sybaseType == SybaseType.SYBASE_TINYINT_TYPE)
			return BYTE;
		else if(sybaseType == SybaseType.SYBASE_BIT_TYPE)
			return BOOLEAN;
		else if((sybaseType == SybaseType.SYBASE_IMAGE_TYPE) || (sybaseType == SybaseType.SYBASE_VARBINARY_TYPE)
				|| (sybaseType == SybaseType.SYBASE_BIANRY_TYPE))
			return BLOB;

		return null;
	}

	public static ColumnType getColumnTypeFromSybaseSchema(SybaseTableColumn columnSchema)
	{
		return getColumnTypeFromSybaseSchema(columnSchema.sybaseType, columnSchema.length);
	}

	public static String[] getJavaDefinitionFromSybaseSchema(SybaseTableColumn columnSchema, boolean isPartOfKey, boolean annotate, boolean isAffinity)
	{
		if((columnSchema.sybaseType == SybaseType.SYBASE_INT_TYPE) || (columnSchema.sybaseType == SybaseType.SYBASE_INT_TYPE2))
			return INT.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		else if((columnSchema.sybaseType == SybaseType.SYBASE_FLOAT_TYPE) || (columnSchema.sybaseType == SybaseType.SYBASE_FLOAT_TYPE2))
			return DOUBLE.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		else if(columnSchema.sybaseType == SybaseType.SYBASE_TEXT_TYPE)
			return STRING.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		else if((columnSchema.sybaseType == SybaseType.SYBASE_VARCHAR_TYPE) || (columnSchema.sybaseType == SybaseType.SYBASE_CHAR_TYPE))
			return STRING.getJavaDefinitionString(columnSchema, isPartOfKey, annotate, columnSchema.length, isAffinity);
		/*
		 * else if(columnSchema.sybaseType == SybaseType.SYBASE_CHAR_TYPE)
		 * return CHAR.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		 */
		else if((columnSchema.sybaseType == SybaseType.SYBASE_DATETIME_TYPE) || (columnSchema.sybaseType == SybaseType.SYBASE_DATETIME_TYPE2)
				|| (columnSchema.sybaseType == SybaseType.SYBASE_TIME_TYPE) || (columnSchema.sybaseType == SybaseType.SYBASE_SMALLDATETIME_TYPE)
				|| (columnSchema.sybaseType == SybaseType.SYBASE_DATE_TYPE))
			return DATE.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		else if((columnSchema.sybaseType == SybaseType.SYBASE_NUMERIC_TYPE) || (columnSchema.sybaseType == SybaseType.SYBASE_NUMERIC_TYPE2)
				|| (columnSchema.sybaseType == SybaseType.SYBASE_DECIMAL_TYPE))
			return DECIMAL.getJavaDefinitionString(columnSchema, isPartOfKey, annotate, columnSchema.length, isAffinity);
		else if(columnSchema.sybaseType == SybaseType.SYBASE_SHORT_TYPE)
			return SHORT.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		else if(columnSchema.sybaseType == SybaseType.SYBASE_TINYINT_TYPE)
			return BYTE.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		else if(columnSchema.sybaseType == SybaseType.SYBASE_BIT_TYPE)
			return BOOLEAN.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);
		else if((columnSchema.sybaseType == SybaseType.SYBASE_IMAGE_TYPE) || (columnSchema.sybaseType == SybaseType.SYBASE_VARBINARY_TYPE)
				|| (columnSchema.sybaseType == SybaseType.SYBASE_BIANRY_TYPE))
			return BLOB.getJavaDefinition(columnSchema, isPartOfKey, annotate, isAffinity);

		return null;
	}

	private static interface ColumnTypeComparator extends Comparator<Object>
	{
		public Comparator<Object> BOOLEAN = new BooleanComparator();

		// public Comparator<Object> CHAR = new CharComparator();
		public Comparator<Object> INT = new IntComparator();

		public Comparator<Object> BYTE = new ByteComparator();

		public Comparator<Object> SHORT = new ShortComparator();

		public Comparator<Object> STRING = new StringComparator();

		public Comparator<Object> BLOB = new BlobComparator();

		public Comparator<Object> DATE = new DateComparator();

		public Comparator<Object> DECIMAL = new BigDecimalComparator();

		public Comparator<Object> DOUBLE = new DoubleComparator();

		static class BooleanComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return Boolean.compare((boolean) o1, (boolean) o2);
			}
		}

		static class IntComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return Integer.compare((int) o1, (int) o2);
			}
		}

		/*
		 * static class CharComparator implements ColumnType.ColumnTypeComparator
		 * {
		 * @Override
		 * public int compare(Object o1, Object o2)
		 * {
		 * return Character.compare((char) o1, (char) o2);
		 * }
		 * }
		 */

		static class ByteComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return Byte.compare((byte) o1, (byte) o2);
			}
		}

		static class ShortComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return Short.compare((short) o1, (short) o2);
			}
		}

		static class DoubleComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return Double.compare((double) o1, (double) o2);
			}
		}

		static class StringComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return ((String) o1).compareTo((String) o2);
			}
		}

		static class BigDecimalComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return ((BigDecimal) o1).compareTo((BigDecimal) o2);
			}
		}

		static class DateComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				if(o1 instanceof Date)
					return ((Date) o1).compareTo((Date) o2);

				return ((java.util.Date) o1).compareTo((java.util.Date) o2);
			}
		}

		static class BlobComparator implements ColumnType.ColumnTypeComparator
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				byte[] b1 = (byte[]) o1;
				byte[] b2 = (byte[]) o2;

				for(int i = 0; (i < b1.length) && (i < b2.length); i++)
				{
					int diff = b1[i] - b2[i];

					if(diff != 0)
						return diff;
				}

				if(b1.length == b2.length)
					return 0;
				else if(b1.length > b2.length)
					return 1;

				return -1;
			}
		}
	}
}