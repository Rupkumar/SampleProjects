package com.aladdin.aqs;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aladdin.aqs.coldboot.ColdbootPolicy;
import com.aladdin.aqs.coldboot.Coldbooter;
import com.aladdin.cloud.AladdinCloud;
import com.aladdin.cloud.AladdinCloudServer;

public class AladdinLiveQueryServerImpl extends AbstractAladdinLiveQueryServer
{
	private static final Logger LOG = LogManager.getLogger(AladdinLiveQueryServerImpl.class);

	private final Coldbooter coldboot;

	AladdinLiveQueryServerImpl(AladdinCloudServer aladdinCloud, String serverName) throws ClassNotFoundException, IOException
	{
		this(aladdinCloud, serverName, null);
	}

	AladdinLiveQueryServerImpl(AladdinCloudServer aladdinCloud, String serverName, Coldbooter coldboot) throws ClassNotFoundException, IOException
	{
		super(aladdinCloud, serverName);

		this.coldboot = coldboot;
	}

	@Override
	public void onColdboot() throws Exception
	{
		if(this.coldboot != null) // IF NULL THEN NO COLDBOOT IS REQUIRED
			this.coldboot.doColdboot(this);
	}

	public static void main(String[] args) throws Exception
	{
		LOG.info("Starting the AladdinCloudServer...");

		/**
		 * Kick off the AladdinCloudServer.
		 *
		 * AladdinCloudServer - Ignite server wrapped so that only relevant functionality in Ignite is accessible.
		 * This will allow us to keep a standard with using Ignite within Aladdin.
		 */
		try(AladdinCloudServer server = AladdinCloud.igniteAladdinCloudServer(args[0]))
		{
			LOG.info("Starting the AladdinLiveQueryServer...");

			/**
			 * Kick off the AladdinLiveQueryServer within the AladdinCloud.
			 */
			AladdinLiveQueryServer alqs = AladdinLiveQueryService.startServer(server, "default_aqs", (qs)->
			{
				/**
				 * Load the coldboot policy from an XML config file.
				 */
				LOG.info("Loading the coldboot policy from file...");
				ColdbootPolicy policy = Coldbooter.buildPolicyFromConfigFile(args[1]);

				/**
				 * Execute the coldboot policy.
				 */
				LOG.info("Executing the coldboot policy...");
				policy.doColdboot(qs);
			}, false);

			/**
			 * Join on the AladdinLiveQueryServer thread so we keep the AladdinCloudServer open while the AladdinLiveQueryServer is open.
			 */
			LOG.info("Joining the AladdinLiveQueryServer thread...");
			alqs.join();
			LOG.info("AladdinLiveQueryServer closed...");
		}
		catch(Exception e)
		{
			LOG.error("Failed to ignite", e);
			throw e;
		}
	}
}