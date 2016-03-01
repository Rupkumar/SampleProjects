package com.aladdin.aqs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aladdin.cloud.AladdinCloud;
import com.aladdin.ignite.AladdinCacheManager;

abstract class AbstractAladdinQueryService<A extends AladdinCloud> extends Thread implements AladdinLiveQueryService<A>
{
	private final static Logger LOG = LogManager.getLogger(AbstractAladdinQueryService.class);

	private final A aladdinCloud;

	private final AladdinCacheManager cacheManager;

	private final String name;

	private boolean blockForColdboot;

	AbstractAladdinQueryService(A ignite, String name)
	{
		super(name + "WorkerThread");

		this.aladdinCloud = ignite;
		this.cacheManager = ignite.getCacheManager();
		this.name = name;
	}

	@Override
	public final A getAladdinCloud()
	{
		return this.aladdinCloud;
	}

	public final AladdinCacheManager getCacheManager()
	{
		return this.cacheManager;
	}

	public final synchronized void start(boolean blockForColdboot) throws Exception
	{
		this.blockForColdboot = blockForColdboot;

		if(blockForColdboot)
		{
			LOG.info("Blocking for init for " + this.name + '!');
			this.onColdboot();
			LOG.info(this.name + " cold booted successfully!");
		}

		LOG.info("Starting " + this.name + " worker...");
		super.start();
	}

	@Override
	public final synchronized void start()
	{
		try
		{
			this.start(true);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public final void run()
	{
		try
		{
			if(!this.blockForColdboot)
			{
				LOG.info(this.name + " worker cold booting...");
				this.onColdboot();
				LOG.info(this.name + " worker cold booted successfully!");
			}

			this.onWorker();
			LOG.info(this.name + " worker completed! Worker thread closing however Aladdin cloud will still remain open as long as resource is open!");
		}
		catch(Throwable th)
		{
			this.onWorkerFailure(th);
		}
	}

	@Override
	public final void interrupt()
	{
		LOG.info(this.name + " has been interupted!");
		this.onWorkerInterrupt();
		super.interrupt();
	}
}