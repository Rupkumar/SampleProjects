package com.aladdin.entities;

import java.io.Serializable;

import org.apache.ignite.cache.store.CacheStoreAdapter;

public abstract class AbstractFromFileEntityCacheStoreAdapter<K extends Serializable, V extends SybaseTable<K>> extends CacheStoreAdapter<K, V>
{

}