package com.rup.ignite.sample.target;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import com.rup.ignite.sample.target.model.AllocTargetMaster;

public class TargetDataCache {

	public static void main(String[] args) throws IgniteException {
		Ignition.setClientMode(true);
		
		try (Ignite ignite = Ignition.start("config/example-ignite.xml")) {
			
			IgniteCache<Long, AllocTargetMaster> targetMasterCache = ignite.getOrCreateCache(TargetCacheConfig.targetMasterCache());
			
			AllocTargetMaster t1 = new AllocTargetMaster();
			t1.setId(2);
			t1.setAllocTargetName("Test2");
			targetMasterCache.put(t1.getId(), t1);
			
			
			String sql1 = "select * from AllocTargetMaster as m";
			
			QueryCursor<List<?>> cursor1 = targetMasterCache.query(new SqlFieldsQuery(sql1));
			System.out.println(cursor1.getAll());
		}

	}
}
