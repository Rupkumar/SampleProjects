package com.aladdin.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class AladdinConnectionPool
{
	private final LinkedBlockingQueue<Connection> availableConnections;

	private final Map<AladdinConnection, Long> openConnections;

	private final ConnectionFactory factory;

	private final int poolSize;

	private Integer createdConnections;

	AladdinConnectionPool(int poolSize, ConnectionFactory factory) throws SQLException
	{
		this(1, poolSize, factory);
	}

	AladdinConnectionPool(int initialSize, int poolSize, ConnectionFactory factory) throws SQLException
	{
		this.availableConnections = new LinkedBlockingQueue<>(poolSize);
		this.openConnections = Collections.synchronizedMap(new IdentityHashMap<>());
		this.factory = factory;
		this.createdConnections = initialSize;
		this.poolSize = poolSize;

		for(int i = 0; i < initialSize; i++)
			this.availableConnections.offer(factory.createConnection());
	}

	private Connection getConnection(long timeout) throws SQLException
	{
		Connection conn = this.availableConnections.poll();

		if(conn == null)
		{
			synchronized(this.createdConnections)
			{
				if(this.createdConnections < this.poolSize)
				{
					conn = this.factory.createConnection();
					this.createdConnections++;
				}
			}

			if(conn == null)
			{
				synchronized(this.availableConnections)
				{
					try
					{
						this.availableConnections.wait(timeout);
					}
					catch(InterruptedException e)
					{}
				}

				return this.getConnection(timeout);
			}
		}

		return conn;
	}

	AladdinConnection checkoutConnection() throws SQLException
	{
		AladdinConnection aladdinConn = new AladdinConnection(this, this.getConnection(1000));
		this.openConnections.put(aladdinConn, System.nanoTime());
		return aladdinConn;
	}

	void closeAladdinConnection(AladdinConnection conn)
	{
		this.openConnections.remove(conn);
		this.availableConnections.offer(conn.getConnection());

		synchronized(this.availableConnections)
		{
			this.availableConnections.notify();
		}
	}
}