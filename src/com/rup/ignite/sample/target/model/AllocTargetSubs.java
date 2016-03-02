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

@AladdinDatabaseObject(getTbl="alloc_target_subs", keyClass=Integer.class)
public final class AllocTargetSubs extends AbstractAladdinEntity<AllocTargetSubsKey> implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    @AladdinColumn(columnName="alloc_target_subs_id", columnType=ColumnType.INT, primaryKey=true)
	@QuerySqlField(name="alloc_target_subs_id",index = true)
	private Integer allocTargetSubsId;
    
    @AladdinColumn(columnName="portfolio_code", columnType=ColumnType.INT)
    @QuerySqlField(name="portfolio_code",index = true)
    private Integer portfolioCode;
    
    @AladdinColumn(columnName="alloc_target_master_id", columnType=ColumnType.INT)
    @QuerySqlField(name="alloc_target_master_id",index = true)
    private Integer allocTargetMasterId;
	
    @AladdinColumn(columnName="start_date", columnType=ColumnType.DATE)
    @QuerySqlField(name="start_date")
	private Date startDate;
    
    @AladdinColumn(columnName="end_date", columnType=ColumnType.DATE)
    @QuerySqlField(name="end_date")
    private Date endDate;

    @AladdinColumn(columnName="run_order", columnType=ColumnType.INT)
    @QuerySqlField(name="run_order")
    private Integer runOrder;
    
    @AladdinColumn(columnName="created_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="created_by")
    private String createdBy;
    
    @AladdinColumn(columnName="created_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="created_time")
    private Date createdTime;
	
    @AladdinColumn(columnName="status", columnType=ColumnType.STRING, length=15)
	@QuerySqlField(name="status")
	private String status;
    
    @AladdinColumn(columnName="final_signoff_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="final_signoff_by")
    private String finalSignOffBy;
    
    @AladdinColumn(columnName="final_signoff_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="final_signoff_time")
    private Date finalSignOffTime;

    @AladdinColumn(columnName="modified_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="modified_by")
	private String modifiedBy;
	
    @AladdinColumn(columnName="modified_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="modified_time")
	private Date modifiedTime;
	
    
    public static AladdinSybaseCache<AllocTargetSubsKey, AllocTargetSubs> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)
    {
        return acm.createSybaseCache(name, AllocTargetSubsKey.class, AllocTargetSubs.class, coldboot, CacheWriteSynchronizationMode.FULL_ASYNC, CacheRebalanceMode.ASYNC, 0, CacheMode.PARTITIONED, CacheAtomicityMode.TRANSACTIONAL);
    }
    
    public AllocTargetSubs() {
        super(null);
    }
   
    public AllocTargetSubs (Integer allocTargetSubsId, Integer portfolioCode, Integer allocTargetMasterId, Date startDate, Date endDate, Integer runOrder, String createdBy, Date createdTime, String status, String finalSignoffBy, Date finalSignoffTime, String modifiedBy, Date modifiedTime) {
        super(new AllocTargetSubsKey(allocTargetSubsId, allocTargetMasterId));

        this.allocTargetSubsId = allocTargetSubsId;
        this.portfolioCode = portfolioCode;
        this.allocTargetMasterId = allocTargetMasterId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.runOrder = runOrder;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.status = status;
        this.finalSignOffBy=finalSignoffBy;
        this.finalSignOffTime = finalSignoffTime;
        this.modifiedBy = modifiedBy;
        this.modifiedTime = modifiedTime;
    }

    public Integer getAllocTargetSubsId() {
        return allocTargetSubsId;
    }

    public void setAllocTargetSubsId(Integer allocTargetSubsId) {
        this.allocTargetSubsId = allocTargetSubsId;
    }

    public Integer getPortfolioCode() {
        return portfolioCode;
    }

    public void setPortfolioCode(Integer portfolioCode) {
        this.portfolioCode = portfolioCode;
    }

    public Integer getAllocTargetMasterId() {
        return allocTargetMasterId;
    }

    public void setAllocTargetMasterId(Integer allocTargetMasterId) {
        this.allocTargetMasterId = allocTargetMasterId;
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

    public Integer getRunOrder() {
        return runOrder;
    }

    public void setRunOrder(Integer runOrder) {
        this.runOrder = runOrder;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
        result = prime * result + ((allocTargetSubsId == null) ? 0 : allocTargetSubsId.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((finalSignOffBy == null) ? 0 : finalSignOffBy.hashCode());
        result = prime * result + ((finalSignOffTime == null) ? 0 : finalSignOffTime.hashCode());
        result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
        result = prime * result + ((modifiedTime == null) ? 0 : modifiedTime.hashCode());
        result = prime * result + ((portfolioCode == null) ? 0 : portfolioCode.hashCode());
        result = prime * result + ((runOrder == null) ? 0 : runOrder.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        AllocTargetSubs other = (AllocTargetSubs) obj;
        if (allocTargetMasterId == null) {
            if (other.allocTargetMasterId != null)
                return false;
        } else if (!allocTargetMasterId.equals(other.allocTargetMasterId))
            return false;
        if (allocTargetSubsId == null) {
            if (other.allocTargetSubsId != null)
                return false;
        } else if (!allocTargetSubsId.equals(other.allocTargetSubsId))
            return false;
        if (createdBy == null) {
            if (other.createdBy != null)
                return false;
        } else if (!createdBy.equals(other.createdBy))
            return false;
        if (createdTime == null) {
            if (other.createdTime != null)
                return false;
        } else if (!createdTime.equals(other.createdTime))
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
        if (portfolioCode == null) {
            if (other.portfolioCode != null)
                return false;
        } else if (!portfolioCode.equals(other.portfolioCode))
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
        return true;
    }
    
    @Override
    public String toString() {
        return "AllocTargetSubs [allocTargetSubsId=" + allocTargetSubsId + ", portfolioCode=" + portfolioCode + ", allocTargetMasterId=" + allocTargetMasterId
                + ", startDate=" + startDate + ", endDate=" + endDate + ", runOrder=" + runOrder + ", createdBy=" + createdBy + ", createdTime=" + createdTime
                + ", status=" + status + ", finalSignOffBy=" + finalSignOffBy + ", finalSignOffTime=" + finalSignOffTime + ", modifiedBy=" + modifiedBy
                + ", modifiedTime=" + modifiedTime + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        out.writeInt(this.allocTargetSubsId);
        out.writeObject(this.portfolioCode);
        out.writeInt(this.allocTargetMasterId);
        out.writeObject(this.startDate);
        out.writeObject(this.endDate);
        out.writeObject(this.runOrder);
        out.writeObject(this.createdBy);
        out.writeObject(this.createdTime);
        out.writeObject(this.status);
        out.writeObject(this.finalSignOffBy);
        out.writeObject(this.finalSignOffTime);
        out.writeObject(this.modifiedBy);
        out.writeObject(this.modifiedTime);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
        this.allocTargetSubsId = in.readInt();
        this.portfolioCode = (Integer)in.readObject();
        this.allocTargetMasterId = in.readInt();
        this.startDate = (Date)in.readObject();
        this.endDate = (Date)in.readObject();
        this.runOrder = (Integer)in.readObject();
        this.createdBy = (String)in.readObject();
        this.createdTime = (Date)in.readObject();
        this.status = (String)in.readObject();
        this.finalSignOffBy = (String)in.readObject();
        this.finalSignOffTime = (Date)in.readObject();
        this.modifiedBy = (String)in.readObject();
        this.modifiedTime = (Date)in.readObject();
    }
	
}
