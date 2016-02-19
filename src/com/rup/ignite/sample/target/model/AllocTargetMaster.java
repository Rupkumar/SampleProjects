package com.rup.ignite.sample.target.model;

import java.util.Date;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class AllocTargetMaster {

	@QuerySqlField(index = true)
	private long id;
	
	private int runOrder;
	
	@QuerySqlField(index = true)
	private Date startDate;
	
	@QuerySqlField(index = true)
	private Date endDate;
	
	private String finalSignOffBy;
	
	private Date finalSignOffTime;
	
	private int at_port_code;
	
	private String modifiedBy;
	
	private Date modifiedTime;
	
	@QuerySqlField(index = true)
	private String status;
	
	@QuerySqlField(index = true)
	private String allocTargetName;
	
	@QuerySqlField(index = true)
	private long allocTargetStrategyId;
	
	@QuerySqlField(index = true)
	private long superId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRunOrder() {
		return runOrder;
	}

	public void setRunOrder(int runOrder) {
		this.runOrder = runOrder;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFinalSignOffBy() {
		return finalSignOffBy;
	}

	public void setFinalSignOffBy(String finalSignOffBy) {
		this.finalSignOffBy = finalSignOffBy;
	}

	public Date getFinalSignOffTime() {
		return finalSignOffTime;
	}

	public void setFinalSignOffTime(Date finalSignOffTime) {
		this.finalSignOffTime = finalSignOffTime;
	}

	public int getAt_port_code() {
		return at_port_code;
	}

	public void setAt_port_code(int at_port_code) {
		this.at_port_code = at_port_code;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAllocTargetName() {
		return allocTargetName;
	}

	public void setAllocTargetName(String allocTargetName) {
		this.allocTargetName = allocTargetName;
	}

	public long getAllocTargetStrategyId() {
		return allocTargetStrategyId;
	}

	public void setAllocTargetStrategyId(long allocTargetStrategyId) {
		this.allocTargetStrategyId = allocTargetStrategyId;
	}

	public long getSuperId() {
		return superId;
	}

	public void setSuperId(long superId) {
		this.superId = superId;
	}

	@Override
	public String toString() {
		return "AllocTargetMaster [id=" + id + ", runOrder=" + runOrder
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", finalSignOffBy=" + finalSignOffBy + ", finalSignOffTime="
				+ finalSignOffTime + ", at_port_code=" + at_port_code
				+ ", modifiedBy=" + modifiedBy + ", modifiedTime="
				+ modifiedTime + ", status=" + status + ", allocTargetName="
				+ allocTargetName + ", allocTargetStrategyId="
				+ allocTargetStrategyId + ", superId=" + superId + "]";
	}
	
}
