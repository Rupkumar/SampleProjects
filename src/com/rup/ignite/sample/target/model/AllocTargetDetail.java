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
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import com.aladdin.cloud.Coldboot;
import com.aladdin.entities.AbstractAladdinEntity;
import com.aladdin.entities.ColumnType;
import com.aladdin.entities.annotations.AladdinColumn;
import com.aladdin.entities.annotations.AladdinDatabaseObject;
import com.aladdin.ignite.AladdinCacheManager;
import com.aladdin.ignite.AladdinSybaseCache;

@AladdinDatabaseObject(getTbl="alloc_target_detail", keyClass=AllocTargetDetailKey.class)
public final class AllocTargetDetail extends AbstractAladdinEntity<AllocTargetDetailKey> implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    @AladdinColumn(columnName="alloc_target_detail_id", columnType=ColumnType.INT, primaryKey=true)
	@QuerySqlField(name="alloc_target_detail_id",index = true)
	private Integer allocTargetDetailId;
    
    @AladdinColumn(columnName="alloc_target_master_id", columnType=ColumnType.INT)
    @QuerySqlField(name="alloc_target_master_id",index = true)
    private Integer allocTargetMasterId;
	
    @AladdinColumn(columnName="display_override_name", columnType=ColumnType.STRING, length=30)
    @QuerySqlField(name="display_override_name")
	private String displayOverrideName;
	
    @AladdinColumn(columnName="alloc_target_type", columnType=ColumnType.STRING, length=15)
	@QuerySqlField(name="alloc_target_type")
	private String allocTargetType;

    @AladdinColumn(columnName="parent_id", columnType=ColumnType.INT)
	@QuerySqlField(name="parent_id")
	private Integer parentId;

    @AladdinColumn(columnName="ordering", columnType=ColumnType.INT)
    @QuerySqlField(name="ordering")
	private Integer ordering;

    @AladdinColumn(columnName="modified_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="modified_by")
	private String modifiedBy;
	
    @AladdinColumn(columnName="modified_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="modified_time")
	private Date modifiedTime;
	
    
    public static AladdinSybaseCache<AllocTargetDetailKey, AllocTargetDetail> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)
    {
        return acm.createSybaseCache(name, AllocTargetDetailKey.class, AllocTargetDetail.class, coldboot, CacheWriteSynchronizationMode.FULL_ASYNC, CacheRebalanceMode.ASYNC, 0, CacheMode.PARTITIONED, CacheAtomicityMode.TRANSACTIONAL);
    }
    
    public AllocTargetDetail() {
        super(null);
    }
   
    public AllocTargetDetail (Integer allocTargetDetailId, Integer allocTargetMasterId, String displayOverrideName, String allocTargetType, Integer parentId, Integer ordering, String modifiedBy, Date modifiedTime) {
        super(new AllocTargetDetailKey(allocTargetDetailId, allocTargetMasterId));

        this.allocTargetDetailId = allocTargetDetailId;
        this.allocTargetMasterId = allocTargetMasterId;
        this.displayOverrideName = displayOverrideName;
        this.allocTargetType = allocTargetType;
        this.parentId = parentId;
        this.ordering = ordering;
        this.modifiedBy = modifiedBy;
        this.modifiedTime = modifiedTime;
    }

    
    public Integer getAllocTargetDetailId() {
        return allocTargetDetailId;
    }

    public void setAllocTargetDetailId(Integer allocTargetDetailId) {
        this.allocTargetDetailId = allocTargetDetailId;
    }

    public Integer getAllocTargetMasterId() {
        return allocTargetMasterId;
    }

    public void setAllocTargetMasterId(Integer allocTargetMasterId) {
        this.allocTargetMasterId = allocTargetMasterId;
    }

    public String getDisplayOverrideName() {
        return displayOverrideName;
    }

    public void setDisplayOverrideName(String displayOverrideName) {
        this.displayOverrideName = displayOverrideName;
    }

    public String getAllocTargetType() {
        return allocTargetType;
    }

    public void setAllocTargetType(String allocTargetType) {
        this.allocTargetType = allocTargetType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
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
    public String toString() {
        return "AllocTargetDetail [allocTargetDetailId=" + allocTargetDetailId + ", allocTargetMasterId=" + allocTargetMasterId + ", displayOverrideName="
                + displayOverrideName + ", allocTargetType=" + allocTargetType + ", parentId=" + parentId + ", ordering=" + ordering + ", modifiedBy="
                + modifiedBy + ", modifiedTime=" + modifiedTime + "]";
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetDetailId == null) ? 0 : allocTargetDetailId.hashCode());
        result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
        result = prime * result + ((allocTargetType == null) ? 0 : allocTargetType.hashCode());
        result = prime * result + ((displayOverrideName == null) ? 0 : displayOverrideName.hashCode());
        result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
        result = prime * result + ((modifiedTime == null) ? 0 : modifiedTime.hashCode());
        result = prime * result + ((ordering == null) ? 0 : ordering.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
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
        AllocTargetDetail other = (AllocTargetDetail) obj;
        if (allocTargetDetailId == null) {
            if (other.allocTargetDetailId != null)
                return false;
        } else if (!allocTargetDetailId.equals(other.allocTargetDetailId))
            return false;
        if (allocTargetMasterId == null) {
            if (other.allocTargetMasterId != null)
                return false;
        } else if (!allocTargetMasterId.equals(other.allocTargetMasterId))
            return false;
        if (allocTargetType == null) {
            if (other.allocTargetType != null)
                return false;
        } else if (!allocTargetType.equals(other.allocTargetType))
            return false;
        if (displayOverrideName == null) {
            if (other.displayOverrideName != null)
                return false;
        } else if (!displayOverrideName.equals(other.displayOverrideName))
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
        if (ordering == null) {
            if (other.ordering != null)
                return false;
        } else if (!ordering.equals(other.ordering))
            return false;
        if (parentId == null) {
            if (other.parentId != null)
                return false;
        } else if (!parentId.equals(other.parentId))
            return false;
        return true;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        out.writeInt(this.allocTargetDetailId);
        out.writeInt(this.allocTargetMasterId);
        out.writeObject(this.displayOverrideName);
        out.writeObject(this.allocTargetType);
        out.writeObject(this.parentId);
        out.writeObject(this.ordering);
        out.writeObject(this.modifiedBy);
        out.writeObject(this.modifiedTime);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
        this.allocTargetDetailId = in.readInt();
        this.allocTargetMasterId = in.readInt();
        this.displayOverrideName = (String)in.readObject();
        this.allocTargetType = (String)in.readObject();
        this.parentId = (Integer)in.readObject();
        this.ordering = (Integer)in.readObject();
        this.modifiedBy = (String)in.readObject();
        this.modifiedTime = (Date)in.readObject();
    }
	
}
