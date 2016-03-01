package com.aladdin.entities;

import java.io.Externalizable;
import java.io.Serializable;

@SuppressWarnings("unchecked")
public interface SybaseTable<K extends Serializable> extends Externalizable
{
	K getKey();

	default Class<? extends K> getKeyClass()
	{
		K key = this.getKey();

		if(key == null)
			throw new RuntimeException("The key cannot be null without overriding this method!");

		return (Class<? extends K>) key.getClass();
	}

	default Class<? extends SybaseTable<K>> getEntityClass()
	{
		return (Class<? extends SybaseTable<K>>) this.getClass();
	}
}