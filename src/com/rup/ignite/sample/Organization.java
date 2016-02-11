package com.rup.ignite.sample;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Organization implements Serializable {
	
	@QuerySqlField(index = true)
	private long id;
	
	@QuerySqlField(index = true)
	private String name;
	
	Organization(long id, String name) {
		this.id = id; 
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Organization [id=" + id + ", name=" + name + "]";
	}
}
