package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import com.aladdin.cloud.Coldboot;
import com.aladdin.entities.AbstractAladdinEntity;
import com.aladdin.entities.ColumnType;
import com.aladdin.entities.annotations.AladdinColumn;
import com.aladdin.entities.annotations.AladdinDatabaseObject;
import com.aladdin.ignite.AladdinCacheManager;
import com.aladdin.ignite.AladdinSybaseCache;

@AladdinDatabaseObject(getTbl="alloc_target_master", keyClass=Integer.class)
public final class AllocTargetMaster extends AbstractAladdinEntity<Integer> implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    @AladdinColumn(columnName="alloc_target_master_id", columnType=ColumnType.INT, primaryKey=true)
	@QuerySqlField(name="alloc_target_master_id",index = true)
	private Integer allocTargetMasterId;
	
    @AladdinColumn(columnName="run_order", columnType=ColumnType.INT)
    @QuerySqlField(name="run_order")
	private Integer runOrder;
	
    @AladdinColumn(columnName="start_date", columnType=ColumnType.DATE)
	@QuerySqlField(name="start_date", orderedGroups={@QuerySqlField.Group(name="idx_alloc_target_master", order=0, descending = true)})
	private Date startDate;

    @AladdinColumn(columnName="end_date", columnType=ColumnType.DATE)
	@QuerySqlField(name="end_date", orderedGroups={@QuerySqlField.Group(name="idx_alloc_target_master", order=1, descending = true)})
	private Date endDate;

    @AladdinColumn(columnName="final_signoff_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="final_signoff_by")
	private String finalSignOffBy;
	
    @AladdinColumn(columnName="final_signoff_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="final_signoff_time")
	private Date finalSignOffTime;

    @AladdinColumn(columnName="at_port_code", columnType=ColumnType.INT)
    @QuerySqlField(name="at_port_code")
	private Integer atPortCode;
	
    @AladdinColumn(columnName="modified_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="modified_by")
	private String modifiedBy;
	
    @AladdinColumn(columnName="modified_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="modified_time")
	private Date modifiedTime;
	
    @AladdinColumn(columnName="status", columnType=ColumnType.STRING, length=15)
	@QuerySqlField(name="status")
	private String status;
	
    @AladdinColumn(columnName="alloc_target_name", columnType=ColumnType.STRING, length=255)
    @QuerySqlField(name="alloc_target_name")
	private String allocTargetName;

    @AladdinColumn(columnName="alloc_target_strategy_id", columnType=ColumnType.INT)
	@QuerySqlField(name="alloc_target_strategy_id", index = true, orderedGroups={@QuerySqlField.Group(name="idx_alloc_target_master", order=2)})
	private Integer allocTargetStrategyId;

    @AladdinColumn(columnName="super_id", columnType=ColumnType.INT)
    @QuerySqlField(name="super_id")
	private Integer superId;
    
    public static AladdinSybaseCache<Integer, AllocTargetMaster> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)
    {
        return acm.createSybaseCache(name, Integer.class, AllocTargetMaster.class, coldboot, CacheWriteSynchronizationMode.FULL_ASYNC, CacheRebalanceMode.ASYNC, 0, CacheMode.PARTITIONED, CacheAtomicityMode.TRANSACTIONAL);
    }
    
    public AllocTargetMaster() {
        super(null);
    }
   
    public AllocTargetMaster (Integer allocTargetMasterId, Integer runOrder, Date startDate, Date endDate, String finalSignOffBy, Date finalSignOffTime, Integer atPortCode, String modifiedBy, Date modifiedTime, String status, String allocTargetName, Integer allocTargetStrategyId, Integer superId) {
        super(new Integer(allocTargetMasterId));

        this.allocTargetMasterId = allocTargetMasterId;
        this.runOrder = runOrder;
        this.startDate = startDate;
        this.endDate = endDate;
        this.finalSignOffBy = finalSignOffBy;
        this.finalSignOffTime = finalSignOffTime;
        this.atPortCode = atPortCode;
        this.modifiedBy = modifiedBy;
        this.modifiedTime = modifiedTime;
        this.status = status;
        this.allocTargetName = allocTargetName;
        this.allocTargetStrategyId = allocTargetStrategyId;
        this.superId = superId;
    }


	public Integer getAllocTargetMasterId() {
		return allocTargetMasterId;
	}

	public void setAllocTargetMasterId(Integer id) {
		this.allocTargetMasterId = id;
	}

	public Integer getRunOrder() {
		return runOrder;
	}

	public void setRunOrder(Integer runOrder) {
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

	public Integer getAtPortCode() {
		return atPortCode;
	}

	public void setAtPortCode(Integer atPortCode) {
		this.atPortCode = atPortCode;
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

	public Integer getAllocTargetStrategyId() {
		return allocTargetStrategyId;
	}

	public void setAllocTargetStrategyId(Integer allocTargetStrategyId) {
		this.allocTargetStrategyId = allocTargetStrategyId;
	}

	public Integer getSuperId() {
		return superId;
	}

	public void setSuperId(Integer superId) {
		this.superId = superId;
	}

    @Override
    public String toString() {
        return "AllocTargetMaster [allocTargetMasterId=" + allocTargetMasterId + ", runOrder=" + runOrder + ", startDate=" + startDate + ", endDate=" + endDate
                + ", finalSignOffBy=" + finalSignOffBy + ", finalSignOffTime=" + finalSignOffTime + ", atPortCode=" + atPortCode + ", modifiedBy=" + modifiedBy
                + ", modifiedTime=" + modifiedTime + ", status=" + status + ", allocTargetName=" + allocTargetName + ", allocTargetStrategyId="
                + allocTargetStrategyId + ", superId=" + superId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
        result = prime * result + ((allocTargetName == null) ? 0 : allocTargetName.hashCode());
        result = prime * result + ((allocTargetStrategyId == null) ? 0 : allocTargetStrategyId.hashCode());
        result = prime * result + ((atPortCode == null) ? 0 : atPortCode.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((finalSignOffBy == null) ? 0 : finalSignOffBy.hashCode());
        result = prime * result + ((finalSignOffTime == null) ? 0 : finalSignOffTime.hashCode());
        result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
        result = prime * result + ((modifiedTime == null) ? 0 : modifiedTime.hashCode());
        result = prime * result + ((runOrder == null) ? 0 : runOrder.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((superId == null) ? 0 : superId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AllocTargetMaster other = (AllocTargetMaster) obj;
        if (allocTargetMasterId == null) {
            if (other.allocTargetMasterId != null)
                return false;
        } else if (!allocTargetMasterId.equals(other.allocTargetMasterId))
            return false;
        if (allocTargetName == null) {
            if (other.allocTargetName != null)
                return false;
        } else if (!allocTargetName.equals(other.allocTargetName))
            return false;
        if (allocTargetStrategyId == null) {
            if (other.allocTargetStrategyId != null)
                return false;
        } else if (!allocTargetStrategyId.equals(other.allocTargetStrategyId))
            return false;
        if (atPortCode == null) {
            if (other.atPortCode != null)
                return false;
        } else if (!atPortCode.equals(other.atPortCode))
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (finalSignOffBy == null) {
            if (other.finalSignOffBy != null)
                return false;
        } else if (!finalSignOffBy.equals(other.finalSignOffBy))
            return false;
        if (finalSignOffTime == null) {
            if (other.finalSignOffTime != null)
                return false;
        } else if (!finalSignOffTime.equals(other.finalSignOffTime))
            return false;
        if (modifiedBy == null) {
            if (other.modifiedBy != null)
                return false;
        } else if (!modifiedBy.equals(other.modifiedBy))
            return false;
        if (modifiedTime == null) {
            if (other.modifiedTime != null)
                return false;
        } else if (!modifiedTime.equals(other.modifiedTime))
            return false;
        if (runOrder == null) {
            if (other.runOrder != null)
                return false;
        } else if (!runOrder.equals(other.runOrder))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (superId == null) {
            if (other.superId != null)
                return false;
        } else if (!superId.equals(other.superId))
            return false;
        return true;
    }

    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        out.writeInt(this.allocTargetMasterId);
        out.writeObject(this.runOrder);
        out.writeObject(this.startDate);
        out.writeObject(this.endDate);
        out.writeObject(this.finalSignOffBy);
        out.writeObject(this.finalSignOffTime);
        out.writeObject(this.atPortCode);
        out.writeObject(this.modifiedBy);
        out.writeObject(this.modifiedTime);
        out.writeObject(this.status);
        out.writeObject(this.allocTargetName);
        out.writeObject(this.allocTargetStrategyId);
        out.writeObject(this.superId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
        this.allocTargetMasterId = in.readInt();
        this.runOrder = (Integer)in.readObject();
        this.startDate = (Date)in.readObject();
        this.endDate = (Date)in.readObject();
        this.finalSignOffBy = (String)in.readObject();
        this.finalSignOffTime =(Date)in.readObject();
        this.atPortCode = (Integer)in.readObject();
        this.modifiedBy = (String)in.readObject();
        this.modifiedTime = (Date)in.readObject();
        this.status = (String)in.readObject();
        this.allocTargetName = (String)in.readObject();
        this.allocTargetStrategyId = (Integer)in.readObject();
        this.superId = (Integer)in.readObject();
    }
	
}
