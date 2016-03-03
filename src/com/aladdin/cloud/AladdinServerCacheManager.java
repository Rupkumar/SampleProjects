package com.aladdin.cloud;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMemoryMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.affinity.AffinityKeyMapper;
import org.apache.ignite.configuration.CacheConfiguration;

import com.aladdin.database.DatabaseHelper;
import com.aladdin.entities.ReflectionAladdinSybaseCacheStoreAdapter;
import com.aladdin.entities.SybaseTable;
import com.aladdin.ignite.AladdinCache;
import com.aladdin.ignite.AladdinCacheManager;
import com.aladdin.ignite.AladdinSybaseCache;

class AladdinServerCacheManager implements AladdinCacheManager
{
	private final Ignite ignite;

	private final Map<String, AladdinCache<?, ?>> aladdinCaches;

	private final Map<String, AladdinSybaseCache<?, ?>> entityCaches;

	private final DatabaseHelper db;

	AladdinServerCacheManager(Ignite ignite, DatabaseHelper db)
	{
		this.ignite = ignite;
		this.aladdinCaches = new HashMap<>();
		this.entityCaches = new HashMap<>();
		this.db = db;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> createSybaseCache(String name, Class<K> keyClass, Class<V> valueClass,
			Coldboot coldboot, CacheWriteSynchronizationMode syncMode, CacheRebalanceMode rebalMode, int backups, CacheMode cacheMode,
			CacheAtomicityMode cacheAtomicityMode)
	{
		synchronized(this.entityCaches)
		{
			AladdinSybaseCache<K, V> aladdinSybaseCache = (AladdinSybaseCache<K, V>) this.entityCaches.get(name);

			if(aladdinSybaseCache != null)
				return aladdinSybaseCache;
			else if(this.ignite.atomicLong("chk_" + name, 1, true).getAndIncrement() == 1)
			{
				aladdinSybaseCache = new AladdinSybaseCache<>(this.createCache(
						this.createSybaseCacheConfig(name, keyClass, valueClass, syncMode, rebalMode, backups, cacheMode,
								cacheAtomicityMode)).getCache());

				if(coldboot != null)
					coldboot.setDoColdboot(true);
			}
			else
				while((aladdinSybaseCache = this.getSybaseCache(name)) == null)
					try
					{
						Thread.sleep(100);
					}
					catch(InterruptedException e)
					{}

			this.entityCaches.put(name, aladdinSybaseCache);
			return aladdinSybaseCache;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> createSybaseCache(CacheConfiguration<K, V> config, Coldboot coldboot)
	{
		synchronized(this.entityCaches)
		{
			String name = config.getName();
			AladdinSybaseCache<K, V> aladdinSybaseCache = (AladdinSybaseCache<K, V>) this.entityCaches.get(name);

			if(aladdinSybaseCache != null)
				return aladdinSybaseCache;
			else if(this.ignite.atomicLong("chk_" + name, 1, true).getAndIncrement() == 1)
			{
				aladdinSybaseCache = new AladdinSybaseCache<>(this.createCache(config).getCache());

				if(coldboot != null)
					coldboot.setDoColdboot(true);
			}
			else
				aladdinSybaseCache = this.getSybaseCache(name);

			this.entityCaches.put(name, aladdinSybaseCache);
			return aladdinSybaseCache;
		}
	}

	@Override
	public <K extends Serializable, V extends SybaseTable<K>> CacheConfiguration<K, V> createSybaseCacheConfig(String name, Class<K> keyClass,
			Class<V> valueClass, CacheWriteSynchronizationMode syncMode, CacheRebalanceMode rebalMode, int backups, CacheMode cacheMode,
			CacheAtomicityMode cacheAtomicityMode)
	{
		CacheConfiguration<K, V> config = new CacheConfiguration<>(name);
		config.setAtomicityMode(cacheAtomicityMode);
		config.setCacheMode(cacheMode);
		config.setWriteSynchronizationMode(syncMode);
		config.setCacheStoreFactory(new ReflectionAladdinSybaseCacheStoreAdapter<>(valueClass, this.db).getFactory());
		config.setIndexedTypes(keyClass, valueClass);
		config.setRebalanceMode(rebalMode);
		config.setBackups(backups);
		config.setReadThrough(false);
		config.setWriteThrough(false);
		config.setWriteBehindEnabled(false);
		config.setMemoryMode(CacheMemoryMode.ONHEAP_TIERED);
		return config;
	}

	/*
	 * (non-Javadoc)
	 * @see com.aladdin.ignite.AladdinCacheManager#createCache(java.lang.String, org.apache.ignite.cache.CacheWriteSynchronizationMode,
	 * org.apache.ignite.cache.CacheRebalanceMode)
	 */
	@Override
	public <K extends Serializable, V extends Serializable> AladdinCache<K, V> createCache(String name, CacheWriteSynchronizationMode syncMode,
			CacheRebalanceMode rebalMode)
	{
		CacheConfiguration<K, V> config = new CacheConfiguration<>(name);
		config.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		config.setCacheMode(CacheMode.PARTITIONED);
		config.setReadThrough(false);
		config.setWriteThrough(false);
		config.setWriteSynchronizationMode(syncMode);
		config.setRebalanceMode(rebalMode);
		config.setStartSize(300000);
		return this.createCache(config);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aladdin.ignite.AladdinCacheManager#createCache(org.apache.ignite.configuration.CacheConfiguration)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <K extends Serializable, V extends Serializable> AladdinCache<K, V> createCache(CacheConfiguration<K, V> config)
	{
		synchronized(this.aladdinCaches)
		{
			String name = config.getName();
			AladdinCache<K, V> existing = (AladdinCache<K, V>) this.aladdinCaches.get(name);

			if(existing != null)
				return existing;

			existing = new AladdinCache<>(this.ignite.getOrCreateCache(config));
			this.aladdinCaches.put(name, existing);
			return existing;
		}
	}

    /*
     * (non-Javadoc)
     * @see com.aladdin.ignite.AladdinCacheManager#getEntityCache(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> getSybaseCache(String name)
    {
        synchronized(this.entityCaches)
        {
            AladdinSybaseCache<K, V> existing = (AladdinSybaseCache<K, V>) this.entityCaches.get(name);

            if(existing != null)
                return existing;

            IgniteCache<K, V> cache = this.ignite.cache(name);

            if(cache == null)
                return null;

            this.entityCaches.put(name, existing = new AladdinSybaseCache<>(cache));
            return existing;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.aladdin.ignite.AladdinCacheManager#getEntityCache(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <K extends Serializable, V extends Serializable> AladdinCache<K, V> getCache(String name)
    {
        synchronized(this.entityCaches)
        {
            AladdinCache<K, V> existing = (AladdinCache<K, V>) this.aladdinCaches.get(name);

            if(existing != null)
                return existing;

            IgniteCache<K, V> cache = this.ignite.cache(name);

            if(cache == null)
                return null;

            this.aladdinCaches.put(name, existing = new AladdinCache<>(cache));
            return existing;
        }
    }
}