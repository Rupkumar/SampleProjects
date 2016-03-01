package com.aladdin.ignite;

import java.io.Serializable;

import org.apache.ignite.IgniteCache;

public class AladdinCache<K extends Serializable, V extends Serializable>
{
	private final IgniteCache<K, V> cache;

	public AladdinCache(IgniteCache<K, V> cache)
	{
		this.cache = cache;
	}

	public IgniteCache<K, V> getCache()
	{
		return this.cache;
	}
}