package com.rup.ignite.sample;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.aladdin.cloud.AladdinCloud;
import com.aladdin.entities.SybaseTable;
import com.aladdin.ignite.AladdinSybaseCache;

public class AladdinLiveQueryTests
{
	private static final Logger LOG = LogManager.getLogger(AladdinLiveQueryTests.class);

	@Test
	public void testQueries() throws TimeoutException, SQLException
	{
		/**
		 * Kick off the AladdinCloudServer.
		 *
		 * AladdinCloudServer - Ignite server wrapped so that only relevant functionality in Ignite is accessible.
		 * This will allow us to keep a standard with using Ignite within Aladdin.
		 */
		try(AladdinCloud server = AladdinCloud.igniteAladdinCloudClient("src/resources/localCloudServer.xml "))
		{
			/**
			 * You must get all the caches in the client in order to run the query. Otherwise the query fails. You can use any of the caches.
			 */
			AladdinSybaseCache<Serializable, SybaseTable<Serializable>> cache = server.getCacheManager().getSybaseCache("targetdb.dbo.alloc_target_detail");
			server.getCacheManager().getSybaseCache("targetdb.dbo.alloc_target_master");
			server.getCacheManager().getSybaseCache("targetdb.dbo.alloc_target_strategy");
			server.getCacheManager().getSybaseCache("targetdb.dbo.target_param_val_detail");
			
	//		QueryCursor<List<?>> query2 = cache.query(new SqlFieldsQuery("SELECT count(*) FROM \"targetdb.dbo.alloc_target_detail\".ALLOCTARGETDETAIL d, \"targetdb.dbo.target_param_val_detail\".TARGETPARAMVALDETAIL vd  where vd.alloc_target_detail_id = d.alloc_target_detail_id;"));
		     QueryCursor<List<?>> query2 = cache.query(new SqlFieldsQuery("SELECT count(*) FROM \"targetdb.dbo.alloc_target_master\".ALLOCTARGETMASTER m, \"targetdb.dbo.alloc_target_strategy\".ALLOCTARGETSTRATEGY s where m.alloc_target_strategy_id = s.alloc_target_strategy_id and s.description=\'Allocation Target\';"));
			LOG.info("Count: " + query2.getAll().get(0));
		}
		catch(Throwable th)
		{
			LOG.error("Failed!", th);
			throw th;
		}
	}
}