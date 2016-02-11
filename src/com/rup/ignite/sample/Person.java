package com.rup.ignite.sample;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Person {

	@QuerySqlField(index = true)
	private long id;
	
	@QuerySqlField(index = true)
	private long orgId;
	
	@QuerySqlField
	private String name;
	
	@QuerySqlField(index = true)
	private double salary;
	
	Person(long id, long orgId, String name, double salary) {
		this.id = id;
		this.orgId = orgId;
		this.name = name;
		this.salary = salary;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", orgId=" + orgId + ", name=" + name
				+ ", salary=" + salary + "]";
	}
	
}
