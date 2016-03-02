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

@AladdinDatabaseObject(getTbl="target_param_def", keyClass=Integer.class)
public final class TargetParamDef extends AbstractAladdinEntity<Integer> implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    @AladdinColumn(columnName="target_param_def_id", columnType=ColumnType.INT, primaryKey=true)
	@QuerySqlField(name="target_param_def_id",index = true)
	private Integer targetParamDefId;
	
    @AladdinColumn(columnName="alloc_target_strategy_id", columnType=ColumnType.INT)
    @QuerySqlField(name="alloc_target_strategy_id", index=true)
	private Integer allocTargetStrategyId;
	
    @AladdinColumn(columnName="param_desc", columnType=ColumnType.STRING, length=255)
	@QuerySqlField(name="param_desc")
	private String paramDesc;

    @AladdinColumn(columnName="param_type", columnType=ColumnType.STRING, length=255)
	@QuerySqlField(name="param_type")
	private String paramType;

    @AladdinColumn(columnName="active", columnType=ColumnType.STRING, length=1)
    @QuerySqlField(name="active")
	private String active;
	
    @AladdinColumn(columnName="created_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="created_by")
	private String createdBy;
	
    @AladdinColumn(columnName="created_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="created_time")
	private Date createdTime;
	
    
    public static AladdinSybaseCache<Integer, TargetParamDef> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)
    {
        return acm.createSybaseCache(name, Integer.class, TargetParamDef.class, coldboot, CacheWriteSynchronizationMode.FULL_ASYNC, CacheRebalanceMode.ASYNC, 0, CacheMode.REPLICATED, CacheAtomicityMode.TRANSACTIONAL);
    }
    
    public TargetParamDef() {
        super(null);
    }
   
    public TargetParamDef (Integer targetParamDefId, Integer allocTargetStrategyId, String paramDesc, String paramType, String active, String createdBy, Date createdTime) {
        super(new Integer(targetParamDefId));

        this.targetParamDefId = targetParamDefId;
        this.allocTargetStrategyId = allocTargetStrategyId;
        this.paramDesc = paramDesc;
        this.paramType= paramType;
        this.active = active;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
    }


    public Integer getTargetParamDefId() {
        return targetParamDefId;
    }

    public void setTargetParamDefId(Integer targetParamDefId) {
        this.targetParamDefId = targetParamDefId;
    }

    public Integer getAllocTargetStrategyId() {
        return allocTargetStrategyId;
    }

    public void setAllocTargetStrategyId(Integer allocTargetStrategyId) {
        this.allocTargetStrategyId = allocTargetStrategyId;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((allocTargetStrategyId == null) ? 0 : allocTargetStrategyId.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
        result = prime * result + ((paramDesc == null) ? 0 : paramDesc.hashCode());
        result = prime * result + ((paramType == null) ? 0 : paramType.hashCode());
        result = prime * result + ((targetParamDefId == null) ? 0 : targetParamDefId.hashCode());
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
        TargetParamDef other = (TargetParamDef) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;
        if (allocTargetStrategyId == null) {
            if (other.allocTargetStrategyId != null)
                return false;
        } else if (!allocTargetStrategyId.equals(other.allocTargetStrategyId))
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
        if (paramDesc == null) {
            if (other.paramDesc != null)
                return false;
        } else if (!paramDesc.equals(other.paramDesc))
            return false;
        if (paramType == null) {
            if (other.paramType != null)
                return false;
        } else if (!paramType.equals(other.paramType))
            return false;
        if (targetParamDefId == null) {
            if (other.targetParamDefId != null)
                return false;
        } else if (!targetParamDefId.equals(other.targetParamDefId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "TargetParamDef [targetParamDefId=" + targetParamDefId + ", allocTargetStrategyId=" + allocTargetStrategyId + ", paramDesc=" + paramDesc
                + ", paramType=" + paramType + ", active=" + active + ", createdBy=" + createdBy + ", createdTime=" + createdTime + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        out.writeInt(this.targetParamDefId);
        out.writeObject(this.allocTargetStrategyId);
        out.writeObject(this.paramDesc);
        out.writeObject(this.paramType);
        out.writeObject(this.active);
        out.writeObject(this.createdBy);
        out.writeObject(this.createdTime);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
        this.targetParamDefId = in.readInt();
        this.allocTargetStrategyId = (Integer)in.readObject();
        this.paramDesc = (String)in.readObject();
        this.paramType = (String)in.readObject();
        this.active = (String)in.readObject();
        this.createdBy = (String)in.readObject();
        this.createdTime = (Date)in.readObject();
    }
	
}
