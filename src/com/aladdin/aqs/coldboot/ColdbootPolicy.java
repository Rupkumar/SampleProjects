package com.aladdin.aqs.coldboot;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aladdin.aqs.AladdinLiveQueryService;
import com.aladdin.aqs.ReflectionUtil;
import com.aladdin.entities.SybaseTable;
import com.aladdin.entities.annotations.AladdinDatabaseObject;
import com.aladdin.ignite.AladdinCacheManager;
import com.bfm.util.BFMUtil;

public class ColdbootPolicy implements Coldbooter
{
	private static final String ENTITY_ANNOTATION_NOT_DEFINED_MSG = "Entity class must be annotated with AladdinDatabaseObject!";

	private final List<Cache> caches;

	public ColdbootPolicy(com.aladdin.aqs.coldboot.xml.ColdbootPolicy xmlConfig) throws ClassNotFoundException, IOException
	{
		List<com.aladdin.aqs.coldboot.xml.ColdbootPolicy.Caches.Cache> caches = xmlConfig.getCaches().getCache();
		this.caches = new ArrayList<>(caches.size());

		for(com.aladdin.aqs.coldboot.xml.ColdbootPolicy.Caches.Cache cache : caches)
		{
			String clazzName = cache.getClazz();

			if((clazzName != null) && !clazzName.trim().isEmpty())
				this.caches.add(new Cache(cache));
			else
			{
				String pkg = cache.getPackage();

				if((pkg == null) || pkg.trim().isEmpty())
					throw new IllegalArgumentException("Cannot have a null/empty package and class! Must specify one or the other!");

				for(Class<? extends SybaseTable<?>> clazz : ReflectionUtil.getClasses(pkg))
					try
				{
						this.caches.add(new Cache(clazz, cache.getColdbootQuery(), cache.isGpx()));
				}
				catch(ColdbootConfigException ex)
				{
					if(!ex.getMessage().equals(ENTITY_ANNOTATION_NOT_DEFINED_MSG))
						throw ex;
				}
			}
		}
	}

	private abstract class AbstractCache
	{
		final Class<? extends Serializable> keyClass;

		final Class<? extends SybaseTable<?>> entityClass;

		final String cacheName;

		final String coldbootQuery;

		final boolean isGPX;

		@SuppressWarnings("unchecked")
		AbstractCache(String cacheName, String entityClass, String coldbootQuery, boolean isGPX)
		{
			try
			{
				this.entityClass = (Class<? extends SybaseTable<?>>) Class.forName(entityClass);

				if(!this.entityClass.isAnnotationPresent(AladdinDatabaseObject.class))
					throw new ColdbootConfigException(ENTITY_ANNOTATION_NOT_DEFINED_MSG);

				AladdinDatabaseObject entityAnotation = this.entityClass.getAnnotation(AladdinDatabaseObject.class);
				String tbl = BFMUtil.getTbl(entityAnotation.getTbl());
				this.coldbootQuery = Optional.ofNullable(coldbootQuery).orElse("SELECT * FROM " + tbl);
				this.keyClass = entityAnotation.keyClass();
				this.cacheName = cacheName == null ? tbl : cacheName;
				this.isGPX = isGPX;
			}
			catch(ClassNotFoundException e)
			{
				throw new ColdbootConfigException("Class not found when setting up coldboot policy!", e);
			}
		}
	}

	private final class Cache extends AbstractCache
	{
		Cache(com.aladdin.aqs.coldboot.xml.ColdbootPolicy.Caches.Cache cache)
		{
			super(cache.getName(), cache.getClazz(), cache.getColdbootQuery(), cache.isGpx());
		}

		Cache(Class<? extends SybaseTable<?>> clazz, String coldbootQuery, boolean isGPX)
		{
			super(null, clazz.getName(), coldbootQuery, isGPX);
		}
	}

	@Override
	public void doColdboot(AladdinLiveQueryService<?> queryService)
	{
		AladdinCacheManager cacheManager = queryService.getAladdinCloud().getCacheManager();

		try
		{
			for(Cache cache : this.caches)
				new Thread(()->
				{
					try
					{
						ReflectionUtil.coldbootCache(cacheManager, cache.cacheName, cache.keyClass, cache.entityClass, cache.isGPX, cache.coldbootQuery);
					}
					catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
					{
						throw new RuntimeException("Failed to coldboot cache: " + cache.cacheName, e);
					}
				}).start();
		}
		catch(Throwable th)
		{
			throw new RuntimeException("Failed to do coldboot!", th);
		}
	}
}