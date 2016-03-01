package com.aladdin.ignite;

import java.io.Serializable;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;

import com.aladdin.cloud.Coldboot;
import com.aladdin.entities.SybaseTable;

public interface AladdinCacheManager
{
	public <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> createSybaseCache(String name, Class<K> keyClass, Class<V> valueClass,
			Coldboot coldboot, CacheWriteSynchronizationMode syncMode, CacheRebalanceMode rebalMode, int backups, CacheMode cacheMode,
			CacheAtomicityMode cacheAtomicityMode);

	public abstract <K extends Serializable, V extends Serializable> AladdinCache<K, V> createCache(String name, CacheWriteSynchronizationMode syncMode,
			CacheRebalanceMode rebalMode);

	public abstract <K extends Serializable, V extends Serializable> AladdinCache<K, V> createCache(CacheConfiguration<K, V> config);

	public abstract <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> getSybaseCache(String name);

	public <K extends Serializable, V extends SybaseTable<K>> CacheConfiguration<K, V> createSybaseCacheConfig(String name, Class<K> keyClass,
			Class<V> valueClass, CacheWriteSynchronizationMode syncMode, CacheRebalanceMode rebalMode, int backups, CacheMode cacheMode,
			CacheAtomicityMode cacheAtomicityMode);

	public abstract <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> createSybaseCache(CacheConfiguration<K, V> config,
			Coldboot coldboot);

	 <K extends Serializable, V extends Serializable> AladdinCache<K, V> getCache(String name);
}