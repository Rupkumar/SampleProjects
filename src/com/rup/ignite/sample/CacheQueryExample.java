package com.rup.ignite.sample;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

public class CacheQueryExample {

	public static void main(String[] args) throws IgniteException {
		Ignition.setClientMode(true);
		
		try (Ignite ignite = Ignition.start("config/example-ignite.xml")) {
			
			CacheConfiguration<Long, Organization> orgCfg = new CacheConfiguration<>("orgCache");
			orgCfg.setCacheMode(CacheMode.REPLICATED);
			orgCfg.setIndexedTypes(Long.class, Organization.class);
			
			IgniteCache<Long, Organization> orgCache = ignite.getOrCreateCache(orgCfg);
			
			CacheConfiguration<Long, Person> personCfg = new CacheConfiguration<>("personCache");
			personCfg.setCacheMode(CacheMode.PARTITIONED);
			personCfg.setIndexedTypes(Long.class, Person.class);
			
			IgniteCache<Long, Person> personCache = ignite.getOrCreateCache(personCfg);
			
			/*
			Organization org1 = new Organization(101, "GridGain");
			Organization org2 = new Organization(102, "Other");
			
			orgCache.put(org1.getId(), org1);
			orgCache.put(org2.getId(), org2);
			
			Person p1 = new Person(1, org1.getId(), "Jon Doe", 2000);
			Person p2 = new Person(2, org1.getId(), "Alex Smith", 6000);
			Person p3 = new Person(3, org2.getId(), "Rob chan", 5000);
			
			personCache.put(p1.getId(), p1);
			personCache.put(p2.getId(), p2);
			personCache.put(p3.getId(), p3);
			*/
			
			String sql1 = "select p.name from Person as p, \"orgCache\".Organization as org " +
						"where p.orgId = org.Id and org.name = ?";
			
			QueryCursor<List<?>> cursor1 = personCache.query(new SqlFieldsQuery(sql1).setArgs("GridGain"));
			System.out.println(cursor1.getAll());
			
			String sql2 = "select min(salary), max(salary), avg(salary) from Person as p, \"orgCache\".Organization as org " +
					"where p.orgId = org.Id and org.name = ?";
		
			QueryCursor<List<?>> cursor2 = personCache.query(new SqlFieldsQuery(sql2).setArgs("GridGain"));
			System.out.println(cursor2.getAll());
			
			String sql3 = "select org.name, count(p.id) from Person as p, \"orgCache\".Organization as org " +
					"where p.orgId = org.Id group by org.name";
		
			QueryCursor<List<?>> cursor3 = personCache.query(new SqlFieldsQuery(sql3));
			System.out.println(cursor3.getAll());
		}

	}
}
