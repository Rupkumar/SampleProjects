package com.aladdin.aqs;

import com.aladdin.aqs.coldboot.Coldbooter;
import com.aladdin.cloud.AladdinCloud;
import com.aladdin.cloud.AladdinCloudServer;

public interface AladdinLiveQueryService<A extends AladdinCloud>
{
	/**
	 * Gets the name of the query server.
	 *
	 * @return the name of the server.
	 */
	String getName();

	A getAladdinCloud();

	void join() throws InterruptedException;

	void join(long millis) throws InterruptedException;

	/**
	 * Starts the server. If blockForColdboot then this method will block until coldboot is completed.
	 * @param blockForColdboot
	 * @throws Exception
	 */
	void start(boolean blockForColdboot) throws Exception;

	/**
	 * Starts the server and blocks for coldboot.
	 * @throws Exception
	 */
	default void start() throws Exception
	{
		this.start(true);
	}

	/**
	 * This method should implement anything necessary to coldboot the cache on
	 * start of the query server.
	 * @throws Exception
	 */
	default void onColdboot() throws Exception
	{
	}

	/**
	 * This method should implement any tasks to be handled be the worker
	 * thread. By default it just holds the thread open by sleeping.
	 */
	default void onWorker() throws Throwable
	{
		while(true)
			Thread.sleep(100000l);
	}

	/**
	 * This method should implement anything necessary when the worker thread is
	 * interrupted.
	 */
	default void onWorkerInterrupt()
	{
	}

	/**
	 * This method should implement anything necessary to handle when the worker
	 * thread throws something.
	 *
	 * @param th the throwable from the worker thread
	 * @throws Throwable
	 */
	default void onWorkerFailure(Throwable th)
	{
		throw new RuntimeException(this.getName() + " worked failed!", th);
	}

	public static AladdinLiveQueryServer startServer(AladdinCloudServer aladdinCloud, String serverName) throws Exception
	{
		return startServer(aladdinCloud, serverName, (Coldbooter) null);
	}

	public static AladdinLiveQueryServer startServer(AladdinCloudServer aladdinCloud, String serverName, Coldbooter coldboot) throws Exception
	{
		return startServer(aladdinCloud, serverName, coldboot, true);
	}

	public static AladdinLiveQueryServer startServer(AladdinCloudServer aladdinCloud, String serverName, Coldbooter coldboot, boolean blockColdboot)
			throws Exception
	{
		AladdinLiveQueryServerImpl server = new AladdinLiveQueryServerImpl(aladdinCloud, serverName, coldboot);
		server.start(blockColdboot);
		return server;
	}
}