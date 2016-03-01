package com.aladdin.database;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseHelper
{
	private static final Logger LOG = LogManager.getLogger(DatabaseHelper.class);

	private volatile DataSource writeDataSource;

	private volatile DataSource readDataSource;

	private volatile DataSource gpxDataSource;

	private volatile boolean isSet;

	private final static DatabaseHelper INSTANCE;

	static
	{
		INSTANCE = new DatabaseHelper();
	}

	public static DatabaseHelper getInstance()
	{
		return INSTANCE;
	}

	public static DatabaseHelper setup(DataSource writeDataSource, DataSource readDataSource, DataSource gpxDataSource)
	{
		try
		{
			if(INSTANCE.writeDataSource != null)
				return INSTANCE;
			else if(writeDataSource == null)
				throw new RuntimeException("Write data source cannot be null!");
			else if(readDataSource == null)
				throw new RuntimeException("Read data source cannot be null!");
			else if(gpxDataSource == null)
				throw new RuntimeException("GPX data source cannot be null!");

			LOG.info("LOADING INSTANCE!");

			synchronized(INSTANCE)
			{
				INSTANCE.writeDataSource = writeDataSource;
				INSTANCE.readDataSource = readDataSource;
				INSTANCE.gpxDataSource = gpxDataSource;
				INSTANCE.isSet = true;
			}

			LOG.info("LOADED INSTANCE!");
			return INSTANCE;
		}
		catch(Throwable th)
		{
			LOG.error("Failed to start database helper!", th);
			throw th;
		}
	}

	private DatabaseHelper()
	{
	}

	public AladdinConnection getReadConnection() throws SQLException
	{
		boolean isSet = false;

		while(!isSet)
		{
			synchronized(this)
			{
				isSet = this.isSet;
			}

			if(!isSet)
				try
				{
					Thread.sleep(10);
				}
				catch(InterruptedException e)
				{}
		}

		return new AladdinConnection(null, this.readDataSource.getConnection());
	}

	public AladdinConnection getWriteConnection() throws SQLException
	{
		boolean isSet = false;

		while(!isSet)
		{
			synchronized(this)
			{
				isSet = this.isSet;
			}

			if(!isSet)
				try
				{
					Thread.sleep(10);
				}
				catch(InterruptedException e)
				{}
		}

		return new AladdinConnection(null, this.writeDataSource.getConnection());
	}

	public AladdinConnection getGPXConnection() throws SQLException
	{
		boolean isSet = false;

		while(!isSet)
		{
			LOG.info("Checking...");

			synchronized(this)
			{
				isSet = this.isSet;
			}

			if(!isSet)
				try
				{
					Thread.sleep(10);
				}
				catch(InterruptedException e)
				{}
		}

		return new AladdinConnection(null, this.gpxDataSource.getConnection());
	}
}