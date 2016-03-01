package com.aladdin.sybase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.aladdin.entities.ColumnType;

public final class SybaseTableIndexInfo
{
	final IndexType type;

	private final SybaseTableSchema table;

	public final String index_name;

	public final List<String> keys;

	public enum IndexType
	{
		PX, UX, IX;

		public boolean isPrimary()
		{
			return this == PX;
		}

		public boolean isKey()
		{
			return this != IX;
		}
	}

	SybaseTableIndexInfo(SybaseTableSchema table, String index_name, IndexType type, String key1, String key2, String key3, String key4, String key5,
			String key6, String key7, String key8, String key9, String key10, String key11, String key12, String key13, String key14, String key15,
			String key16, String key17, String key18, String key19, String key20, String key21, String key22, String key23, String key24, String key25,
			String key26, String key27, String key28, String key29, String key30, String key31, String key32)
			{
		this.table = table;
		this.index_name = index_name;
		this.type = type;
		this.keys = new LinkedList<>();

		if(key1 == null)
			throw new RuntimeException("key1 can't be null!");

		this.keys.add(key1);

		if(key2 == null)
			return;

		this.keys.add(key2);

		if(key3 == null)
			return;

		this.keys.add(key3);

		if(key4 == null)
			return;

		this.keys.add(key4);

		if(key5 == null)
			return;

		this.keys.add(key5);

		if(key6 == null)
			return;

		this.keys.add(key6);

		if(key7 == null)
			return;

		this.keys.add(key7);

		if(key8 == null)
			return;

		this.keys.add(key8);

		if(key9 == null)
			return;

		this.keys.add(key9);

		if(key10 == null)
			return;

		this.keys.add(key10);

		if(key11 == null)
			return;

		this.keys.add(key11);

		if(key12 == null)
			return;

		this.keys.add(key12);

		if(key13 == null)
			return;

		this.keys.add(key13);

		if(key14 == null)
			return;

		this.keys.add(key14);

		if(key15 == null)
			return;

		this.keys.add(key15);

		if(key16 == null)
			return;

		this.keys.add(key16);

		if(key17 == null)
			return;

		this.keys.add(key17);

		if(key18 == null)
			return;

		this.keys.add(key18);

		if(key19 == null)
			return;

		this.keys.add(key19);

		if(key20 == null)
			return;

		this.keys.add(key20);

		if(key21 == null)
			return;

		this.keys.add(key21);

		if(key22 == null)
			return;

		this.keys.add(key22);

		if(key23 == null)
			return;

		this.keys.add(key23);

		if(key24 == null)
			return;

		this.keys.add(key24);

		if(key25 == null)
			return;

		this.keys.add(key25);

		if(key26 == null)
			return;

		this.keys.add(key26);

		if(key27 == null)
			return;

		this.keys.add(key27);

		if(key28 == null)
			return;

		this.keys.add(key28);

		if(key29 == null)
			return;

		this.keys.add(key29);

		if(key30 == null)
			return;

		this.keys.add(key30);

		if(key31 == null)
			return;

		this.keys.add(key31);

		if(key32 == null)
			return;

		this.keys.add(key32);
			}

	@Override
	public String toString()
	{
		return "SybaseTableIndexInfo [type=" + this.type + ", table_name="
				+ this.table + ", index_name=" + this.index_name + ", keys=" + this.keys
				+ "]";
	}

	int calculateSize(Map<String, SybaseTableColumn> colMap)
	{
		int size = 0;

		for(String key : this.keys)
		{
			SybaseTableColumn col = colMap.get(key);
			ColumnType type = ColumnType.getColumnTypeFromSybaseSchema(col);

			if(type == ColumnType.INT)
				size += Integer.BYTES;
			else if(type == ColumnType.STRING)
				size += col.length;
			else if(type == ColumnType.DOUBLE)
				size += Double.BYTES;
			else if(type == ColumnType.DECIMAL)
				size += Double.BYTES;
			else if(type == ColumnType.SHORT)
				size += Short.BYTES;
			else if(type == ColumnType.DATE)
				size += 10;
			else if(type == ColumnType.BLOB)
				size += Integer.MAX_VALUE;
			else
				throw new RuntimeException(type.name() + " not setup for size calc!");
		}

		return size;
	}
}