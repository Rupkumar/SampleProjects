package com.aladdin.ignite;

import java.io.Serializable;
import java.util.List;

import javax.cache.Cache.Entry;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aladdin.entities.SybaseTable;

/**
 * Aladdin cache for sybase tables.
 * @author chjohnst
 *
 * @param <K> The key of sybase table.
 * @param <V> The sybase table.
 */
public class AladdinSybaseCache<K extends Serializable, V extends SybaseTable<K>>
{
	/**
	 * The logger.
	 */
	private static final Logger LOG = LogManager.getLogger(AladdinSybaseCache.class);

	/**
	 * The ignite cache.
	 */
	private final IgniteCache<K, V> cache;

	
	
	public IgniteCache<K, V> getCache() {
        return cache;
    }

    /**
	 * The constructor.
	 * @param cache The ignite cache.
	 */
	public AladdinSybaseCache(IgniteCache<K, V> cache)
	{
		this.cache = cache;
	}

	/**
	 * Query the cache for the given query.
	 * @param qry The SQL query.
	 * @return The cursor to the results.
	 */
	public QueryCursor<List<?>> query(String qry)
	{
		return this.query(new SqlFieldsQuery(qry));
	}

    public V get(K key) {
        return this.cache.get(key);
    }

	
	/**
	 * Query the cache for the given query.
	 * @param qry The Ignite SQL query.
	 * @return The cursor to the results.
	 */
	public QueryCursor<? extends Entry<K, V>> query(SqlQuery<K, V> qry)
	{
		return this.cache.query(qry);
	}

	/**
	 * Query the cache for the given query.
	 * @param qry The Ignite SqlFieldsQuery query.
	 * @return The cursor to the results.
	 */
	public QueryCursor<List<?>> query(SqlFieldsQuery qry)
	{
		return this.cache.query(qry);
	}

	/**
	 * Start a subscribed query.
	 * @param qry The query.
	 * @return The cursor to the results and notifications.
	 */
	public QueryCursor<Entry<K, V>> subscribedQuery(ContinuousQuery<K, V> qry)
	{
		return this.cache.query(qry);
	}

	/**
	 * Get the size of the cache across all nodes.
	 * @return The size of the cache across all nodes.
	 */
	public int size()
	{
		return this.size(CachePeekMode.ALL);
	}

	public void coldbootCache(IgniteBiPredicate<K, V> obj, Object... args)
	{
		LOG.info("Coldbooting " + this.cache.getName() + "...");
		this.cache.loadCache(obj, args);
		LOG.info(this.cache.getName() + " coldbooted successfully!");
	}

	public int size(CachePeekMode mode)
	{
		return this.cache.size(mode);
	}

	public void insert(V entity)
	{
		this.insert(entity, true);
	}

	/**
	 * Inserts an item into the cache. If failIfExists is true then the entity
	 * is only inserted if the key does not already exists, if the key exists a
	 * KeyAlreadyExists exception is thrown.
	 *
	 * @param entity
	 * @param failIfExists Only thrown if failIfExists is true and the key already exists in the cache.
	 */
	public void insert(V entity, boolean failIfExists)
	{
		K key = entity.getKey();

		if(failIfExists)
		{
			if(!this.cache.putIfAbsent(key, entity))
				throw new IllegalArgumentException("The key already exists! Key: " + key);
		}
		else
			this.cache.put(key, entity);
	}

	/**
	 * Deletes the entity from the cache if the specified entity is the current
	 * item in the cache. If the key of the entity exists but the entity is not
	 * the same as the specified entity then nothing is changed.
	 * @param entity
	 * @return Whether an entity was deleted with the specified key.
	 */
	public boolean delete(V entity)
	{
		return this.cache.remove(entity.getKey(), entity);
	}

	/**
	 * Deletes the key from the cache.
	 * @param key
	 * @return Whether an entity was deleted with the specified key.
	 */
	public boolean delete(K key)
	{
		return this.cache.remove(key);
	}

	/**
	 * Updates the cache with the specified entity.
	 * @param entity
	 * @return Whether the cache object was updated.
	 */
	public boolean update(V entity)
	{
		return this.cache.replace(entity.getKey(), entity);
	}

	/**
	 * Updates the cache with the updated entity if and only if the current
	 * entity is the specified stale entity. If the keys of the updated and
	 * stale entities are not equal an IllegalArgumentException is thrown.
	 * @param updatedEntity
	 * @param staleEntity
	 * @return Whether the cache object was updated.
	 */
	public boolean update(V updatedEntity, V staleEntity)
	{
		if(!updatedEntity.getKey().equals(staleEntity.getKey()))
			throw new IllegalArgumentException("The primary key(s) of the stale entity does not match the primary key(s) of the updated entity!");

		return this.cache.replace(updatedEntity.getKey(), updatedEntity, staleEntity);
	}
}