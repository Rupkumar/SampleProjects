package com.aladdin.entities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public abstract class AbstractAladdinEntity<K extends Serializable> implements SybaseTable<K>
{
	private static final long serialVersionUID = 8948989960556903898L;

	private K key;

	protected AbstractAladdinEntity(K key)
	{
		this.key = key;
	}

	public AbstractAladdinEntity()
	{
	}

	@Override
	public K getKey()
	{
		return this.key;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(this.key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.key = (K) in.readObject();
	}
}