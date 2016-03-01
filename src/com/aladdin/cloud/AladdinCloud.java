package com.aladdin.cloud;

import java.sql.SQLException;

import com.aladdin.database.DatabaseHelper;
import com.aladdin.ignite.AladdinCacheManager;

public interface AladdinCloud extends AutoCloseable
{
	AladdinCacheManager getCacheManager();

	@Override
	void close();

	long getNodeId();

	public static AladdinCloudClient igniteAladdinCloudClient(String configFile) throws SQLException
	{
		return igniteAladdinCloudClient(configFile, 1);
	}

	public static AladdinCloudClient igniteAladdinCloudClient(String configFile, int poolSize) throws SQLException
	{
		return new AladdinCloudClientImpl(configFile, DatabaseHelper.getInstance());
	}

	public static AladdinCloudClient igniteAladdinCloudClient(String configFile, DatabaseHelper db)
	{
		return new AladdinCloudClientImpl(configFile, db);
	}

	public static AladdinCloudServer igniteAladdinCloudServer(String configFile) throws SQLException
	{
		return igniteAladdinCloudServer(configFile, 1);
	}

	public static AladdinCloudServer igniteAladdinCloudServer(String configFile, int poolSize) throws SQLException
	{
		return igniteAladdinCloudServer(configFile, DatabaseHelper.getInstance());
	}

	public static AladdinCloudServer igniteAladdinCloudServer(String configFile, DatabaseHelper db)
	{
		return new AladdinCloudServerImpl(configFile, db);
	}
}