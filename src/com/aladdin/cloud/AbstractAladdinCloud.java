package com.aladdin.cloud;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import com.aladdin.database.DatabaseHelper;
import com.aladdin.ignite.AladdinCacheManager;

public class AbstractAladdinCloud implements AladdinCloud
{
	private static long NODE_ID;

	private final Ignite ignite;

	private final AladdinCacheManager cacheManager;

	private final long nodeId;

	// ADD OTHER IGNITE CLOUD SERVICES

	AbstractAladdinCloud(String file, DatabaseHelper db, boolean client)
	{
		Ignition.setClientMode(client);
		this.ignite = Ignition.start(file);
		this.cacheManager = new AladdinServerCacheManager(this.ignite, db);
		NODE_ID = this.nodeId = this.ignite.atomicLong("node_id", 0l, true).getAndIncrement();
	}

	@Override
	public long getNodeId()
	{
		return this.nodeId;
	}

	@Override
	public AladdinCacheManager getCacheManager()
	{
		return this.cacheManager;
	}

	@Override
	public void close()
	{
		this.ignite.close();
	}

	public static long getSingletonNodeId()
	{
		return NODE_ID;
	}
}