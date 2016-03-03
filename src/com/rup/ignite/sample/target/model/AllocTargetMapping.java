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

@AladdinDatabaseObject(getTbl="alloc_target_mapping", keyClass=AllocTargetMappingKey.class)
public final class AllocTargetMapping extends AbstractAladdinEntity<AllocTargetMappingKey> implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    @AladdinColumn(columnName="alloc_target_master_id", columnType=ColumnType.INT)
    @QuerySqlField(name="alloc_target_master_id",index = true)
    private Integer allocTargetMasterId;
    
    @AladdinColumn(columnName="alloc_target_mapping_id", columnType=ColumnType.INT, primaryKey=true)
	@QuerySqlField(name="alloc_target_mapping_id",index = true)
	private Integer allocTargetMappingId;
    
    @AladdinColumn(columnName="alloc_target_detail_id", columnType=ColumnType.INT)
    @QuerySqlField(name="alloc_target_detail_id",index = true)
    private Integer allocTargetDetailId;
	
    @AladdinColumn(columnName="in_out", columnType=ColumnType.STRING, length=15)
    @QuerySqlField(name="in_out")
	private String inOut;
	
    @AladdinColumn(columnName="mapping_type", columnType=ColumnType.STRING, length=20)
	@QuerySqlField(name="mapping_type")
	private String mappingType;

    @AladdinColumn(columnName="mapping_id", columnType=ColumnType.STRING, length=255)
	@QuerySqlField(name="mapping_id")
	private String mappingId;

    @AladdinColumn(columnName="buy_sell", columnType=ColumnType.STRING, length=15)
    @QuerySqlField(name="buy_sell")
    private String buySell;
    
    @AladdinColumn(columnName="priority", columnType=ColumnType.INT)
    @QuerySqlField(name="priority")
    private Integer priority;

    @AladdinColumn(columnName="modified_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="modified_by")
	private String modifiedBy;
	
    @AladdinColumn(columnName="modified_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="modified_time")
	private Date modifiedTime;
    
    @AladdinColumn(columnName="mapping_sub_type", columnType=ColumnType.STRING,length=20)
    @QuerySqlField(name="mapping_sub_type")
    private String mappingSubType;
    
    @AladdinColumn(columnName="mapping_description", columnType=ColumnType.STRING,length=255)
    @QuerySqlField(name="mapping_description")
    private String mappingDescription;

    
    public static AladdinSybaseCache<AllocTargetMappingKey, AllocTargetMapping> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)
    {
        return acm.createSybaseCache(name, AllocTargetMappingKey.class, AllocTargetMapping.class, coldboot, CacheWriteSynchronizationMode.FULL_ASYNC, CacheRebalanceMode.ASYNC, 0, CacheMode.PARTITIONED, CacheAtomicityMode.TRANSACTIONAL);
    }
    
    public AllocTargetMapping() {
        super(null);
    }
   
    public AllocTargetMapping (Integer allocTargetMasterId, Integer allocTargetMappingId, Integer allocTargetDetailId, String inOut, String mappingType, String mappingId, String buySell, Integer priority, String modifiedBy, Date modifiedTime, String mappingSubType, String mappingDescription) {
        super(new AllocTargetMappingKey(allocTargetMappingId, allocTargetMasterId));

        this.allocTargetMasterId = allocTargetMasterId;
        this.allocTargetMappingId = allocTargetMappingId;
        this.allocTargetDetailId = allocTargetDetailId;
        this.inOut = inOut;
        this.mappingType = mappingType;
        this.mappingId = mappingId;
        this.buySell = buySell;
        this.priority = priority;
        this.modifiedBy = modifiedBy;
        this.modifiedTime = modifiedTime;
        this.mappingSubType = mappingSubType;
        this.mappingDescription = mappingDescription;
    }

    public Integer getAllocTargetMappingId() {
        return allocTargetMappingId;
    }

    public void setAllocTargetMappingId(Integer allocTargetMappingId) {
        this.allocTargetMappingId = allocTargetMappingId;
    }

    public Integer getAllocTargetDetailId() {
        return allocTargetDetailId;
    }

    public void setAllocTargetDetailId(Integer allocTargetDetailId) {
        this.allocTargetDetailId = allocTargetDetailId;
    }

    public String getInOut() {
        return inOut;
    }

    public void setInOut(String inOut) {
        this.inOut = inOut;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public String getMappingSubType() {
        return mappingSubType;
    }

    public void setMappingSubType(String mappingSubType) {
        this.mappingSubType = mappingSubType;
    }

    public String getMappingDescription() {
        return mappingDescription;
    }

    public void setMappingDescription(String mappingDescription) {
        this.mappingDescription = mappingDescription;
    }
    
    public Integer getAllocTargetMasterId() {
        return allocTargetMasterId;
    }

    public void setAllocTargetMasterId(Integer allocTargetMasterId) {
        this.allocTargetMasterId = allocTargetMasterId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetDetailId == null) ? 0 : allocTargetDetailId.hashCode());
        result = prime * result + ((allocTargetMappingId == null) ? 0 : allocTargetMappingId.hashCode());
        result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
        result = prime * result + ((buySell == null) ? 0 : buySell.hashCode());
        result = prime * result + ((inOut == null) ? 0 : inOut.hashCode());
        result = prime * result + ((mappingDescription == null) ? 0 : mappingDescription.hashCode());
        result = prime * result + ((mappingId == null) ? 0 : mappingId.hashCode());
        result = prime * result + ((mappingSubType == null) ? 0 : mappingSubType.hashCode());
        result = prime * result + ((mappingType == null) ? 0 : mappingType.hashCode());
        result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
        result = prime * result + ((modifiedTime == null) ? 0 : modifiedTime.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
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
        AllocTargetMapping other = (AllocTargetMapping) obj;
        if (allocTargetDetailId == null) {
            if (other.allocTargetDetailId != null)
                return false;
        } else if (!allocTargetDetailId.equals(other.allocTargetDetailId))
            return false;
        if (allocTargetMappingId == null) {
            if (other.allocTargetMappingId != null)
                return false;
        } else if (!allocTargetMappingId.equals(other.allocTargetMappingId))
            return false;
        if (allocTargetMasterId == null) {
            if (other.allocTargetMasterId != null)
                return false;
        } else if (!allocTargetMasterId.equals(other.allocTargetMasterId))
            return false;
        if (buySell == null) {
            if (other.buySell != null)
                return false;
        } else if (!buySell.equals(other.buySell))
            return false;
        if (inOut == null) {
            if (other.inOut != null)
                return false;
        } else if (!inOut.equals(other.inOut))
            return false;
        if (mappingDescription == null) {
            if (other.mappingDescription != null)
                return false;
        } else if (!mappingDescription.equals(other.mappingDescription))
            return false;
        if (mappingId == null) {
            if (other.mappingId != null)
                return false;
        } else if (!mappingId.equals(other.mappingId))
            return false;
        if (mappingSubType == null) {
            if (other.mappingSubType != null)
                return false;
        } else if (!mappingSubType.equals(other.mappingSubType))
            return false;
        if (mappingType == null) {
            if (other.mappingType != null)
                return false;
        } else if (!mappingType.equals(other.mappingType))
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
        if (priority == null) {
            if (other.priority != null)
                return false;
        } else if (!priority.equals(other.priority))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AllocTargetMapping [allocTargetMasterId=" + allocTargetMasterId + ", allocTargetMappingId=" + allocTargetMappingId + ", allocTargetDetailId="
                + allocTargetDetailId + ", inOut=" + inOut + ", mappingType=" + mappingType + ", mappingId=" + mappingId + ", buySell=" + buySell
                + ", priority=" + priority + ", modifiedBy=" + modifiedBy + ", modifiedTime=" + modifiedTime + ", mappingSubType=" + mappingSubType
                + ", mappingDescription=" + mappingDescription + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        out.writeInt( this.allocTargetMappingId);
        out.writeInt(this.allocTargetMasterId);
        out.writeInt(this.allocTargetDetailId);
        out.writeObject(this.inOut);
        out.writeObject(this.mappingType);
        out.writeObject(this.mappingId);
        out.writeObject(this.buySell);
        out.writeObject(this.priority);
        out.writeObject(this.modifiedBy);
        out.writeObject(this.modifiedTime);
        out.writeObject(this.mappingSubType);
        out.writeObject(this.mappingDescription);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
        this.allocTargetMappingId = in.readInt();
        this.allocTargetMasterId = in.readInt();
        this.allocTargetDetailId = in.readInt();
        this.inOut = (String)in.readObject();
        this.mappingType = (String)in.readObject();
        this.mappingId = (String)in.readObject();
        this.buySell = (String)in.readObject();
        this.priority = (Integer)in.readObject();
        this.modifiedBy = (String)in.readObject();
        this.modifiedTime = (Date)in.readObject();
        this.mappingSubType = (String)in.readObject();
        this.mappingDescription = (String)in.readObject();
    }
	
}
