package com.aladdin.aqs;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import com.aladdin.cloud.Coldboot;
import com.aladdin.entities.SybaseTable;
import com.aladdin.entities.annotations.AladdinDatabaseObject;
import com.aladdin.ignite.AladdinCacheManager;
import com.aladdin.ignite.AladdinSybaseCache;
import com.bfm.util.BFMUtil;

public class ReflectionUtil
{
	@SuppressWarnings("unchecked")
	public static <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> constructCache(AladdinCacheManager acm, String name,
			Class<?> keyClass, Class<?> entityClass, Coldboot coldboot) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException
	{
		return (AladdinSybaseCache<K, V>) entityClass.getMethod("createCache", AladdinCacheManager.class, String.class, Coldboot.class).invoke(null, acm, name,
				coldboot);
	}

	public static <K extends Serializable, V extends SybaseTable<K>> AladdinSybaseCache<K, V> coldbootCache(AladdinCacheManager acm, String name,
			Class<?> keyClass, Class<?> entityClass, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException
	{
		Coldboot coldboot = new Coldboot();
		AladdinSybaseCache<K, V> cache = constructCache(acm, name, keyClass, entityClass, coldboot);

		if(coldboot.doColdboot())
			cache.coldbootCache(null, args);

		return cache;
	}

	public static void coldbootWithReflection(AladdinCacheManager acm, String name, Class<? extends SybaseTable<?>>[] classes, Map<String, String> loadClasses)
	{
		for(Class<? extends SybaseTable<?>> clazz : classes)
		{
			if(!clazz.isAnnotationPresent(AladdinDatabaseObject.class))
				continue;

			AladdinDatabaseObject dbAnnotation = clazz.getAnnotation(AladdinDatabaseObject.class);
			String tbl = BFMUtil.getTbl(dbAnnotation.getTbl());
			String query = null;

			if((loadClasses != null) && ((query = loadClasses.getOrDefault(tbl, "IGNORE")) != null) && query.equals("IGNORE"))
				continue;

			try
			{
				Coldboot coldboot = new Coldboot();
				AladdinSybaseCache<?, ?> cache = constructCache(acm, name == null ? tbl : name, dbAnnotation.keyClass(), clazz, coldboot);

				if(!coldboot.doColdboot())
					continue;
				else if(query == null)
					query = "SELECT * FROM " + tbl; // NULL DEFAULTS TO SELECT *
				else if(query.trim().isEmpty())
					continue; // IF QUERY IS EMPTY WE DO NOT LOAD THE CACHE

				cache.coldbootCache(null, query);
			}
			catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				throw new RuntimeException("Failed to do a reflective cold boot!", e);
			}
		}
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 *
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends SybaseTable<?>>[] getClasses(String packageName) throws ClassNotFoundException, IOException
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();

		while(resources.hasMoreElements())
		{
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}

		ArrayList<Class<? extends SybaseTable<?>>> classes = new ArrayList<>();

		for(File directory : dirs)
			classes.addAll(findClasses(directory, packageName));

		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private static List<Class<? extends SybaseTable<?>>> findClasses(File directory, String packageName) throws ClassNotFoundException
	{
		List<Class<? extends SybaseTable<?>>> classes = new ArrayList<>();

		if(!directory.exists())
			return classes;

		File[] files = directory.listFiles();

		for(File file : files)
			if(file.isDirectory())
			{
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			}
			else if(file.getName().endsWith(".class"))
				classes.add((Class<? extends SybaseTable<?>>) Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));

		return classes;
	}
}
