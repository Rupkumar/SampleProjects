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

@AladdinDatabaseObject(getTbl="target_param_val_detail", keyClass=TargetParamValDetailKey.class)
public final class TargetParamValDetail extends AbstractAladdinEntity<TargetParamValDetailKey> implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    @AladdinColumn(columnName="alloc_target_master_id", columnType=ColumnType.INT)
    @QuerySqlField(name="alloc_target_master_id", index=true)
    private Integer allocTargetMasterId;
    
    @AladdinColumn(columnName="target_param_val_detail_id", columnType=ColumnType.INT, primaryKey=true)
	@QuerySqlField(name="target_param_val_detail_id",index = true)
	private Integer targetParamValDetailId;
	
    @AladdinColumn(columnName="target_param_def_id", columnType=ColumnType.INT)
    @QuerySqlField(name="target_param_def_id", index=true)
	private Integer targetParamDefId;
    
    @AladdinColumn(columnName="alloc_target_detail_id", columnType=ColumnType.INT)
    @QuerySqlField(name="alloc_target_detail_id", index=true)
    private Integer allocTargetDetailId;
    
    @AladdinColumn(columnName="value_varchar", columnType=ColumnType.STRING, length=255)
	@QuerySqlField(name="value_varchar")
	private String valueVarchar;

    @AladdinColumn(columnName="value_numeric", columnType=ColumnType.DOUBLE)
	@QuerySqlField(name="value_numeric")
	private Double valueNumeric;

    @AladdinColumn(columnName="value_float", columnType=ColumnType.DOUBLE)
    @QuerySqlField(name="value_float")
	private Double valueFloat;

    @AladdinColumn(columnName="value_int", columnType=ColumnType.INT)
    @QuerySqlField(name="value_int")
    private Integer valueInt;
    
    @AladdinColumn(columnName="created_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="created_by")
	private String createdBy;
	
    @AladdinColumn(columnName="created_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="created_time")
	private Date createdTime;
	
    @AladdinColumn(columnName="seq_num", columnType=ColumnType.INT)
    @QuerySqlField(name="seq_num")
    private Integer seqNum;

    
    public static AladdinSybaseCache<TargetParamValDetailKey, TargetParamValDetail> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)
    {
        return acm.createSybaseCache(name, TargetParamValDetailKey.class, TargetParamValDetail.class, coldboot, CacheWriteSynchronizationMode.FULL_ASYNC, CacheRebalanceMode.ASYNC, 0, CacheMode.PARTITIONED, CacheAtomicityMode.TRANSACTIONAL);
    }
    
    public TargetParamValDetail() {
        super(null);
    }
   
    public TargetParamValDetail (Integer allocTargetMasterId, Integer targetParamValDetailId, Integer targetParamDefId, Integer allocTargetDetailId, String valueVarchar, Double valueNumeric, Double valueFloat, Integer valueInt, String createdBy, Date createdTime, Integer seqNum) {
        super(new TargetParamValDetailKey(targetParamValDetailId, allocTargetMasterId));
        this.targetParamValDetailId = targetParamValDetailId;
        this.allocTargetMasterId = allocTargetMasterId;
        this.targetParamDefId = targetParamDefId;
        this.allocTargetDetailId = allocTargetDetailId;
        this.valueVarchar = valueVarchar;
        this.valueNumeric = valueNumeric;
        this.valueFloat = valueFloat;
        this.valueInt = valueInt;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.seqNum = seqNum;
    }

    
    public Integer getTargetParamValDetailId() {
        return targetParamValDetailId;
    }

    public void setTargetParamValDetailId(Integer targetParamValDetailId) {
        this.targetParamValDetailId = targetParamValDetailId;
    }

    public Integer getTargetParamDefId() {
        return targetParamDefId;
    }

    public void setTargetParamDefId(Integer targetParamDefId) {
        this.targetParamDefId = targetParamDefId;
    }

    public Integer getAllocTargetDetailId() {
        return allocTargetDetailId;
    }

    public void setAllocTargetDetailId(Integer allocTargetDetailId) {
        this.allocTargetDetailId = allocTargetDetailId;
    }

    public String getValueVarchar() {
        return valueVarchar;
    }

    public void setValueVarchar(String valueVarchar) {
        this.valueVarchar = valueVarchar;
    }

    public Double getValueNumeric() {
        return valueNumeric;
    }

    public void setValueNumeric(Double valueNumeric) {
        this.valueNumeric = valueNumeric;
    }

    public Double getValueFloat() {
        return valueFloat;
    }

    public void setValueFloat(Double valueFloat) {
        this.valueFloat = valueFloat;
    }

    public Integer getValueInt() {
        return valueInt;
    }

    public void setValueInt(Integer valueInt) {
        this.valueInt = valueInt;
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

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
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
        result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
        result = prime * result + ((seqNum == null) ? 0 : seqNum.hashCode());
        result = prime * result + ((targetParamDefId == null) ? 0 : targetParamDefId.hashCode());
        result = prime * result + ((targetParamValDetailId == null) ? 0 : targetParamValDetailId.hashCode());
        result = prime * result + ((valueFloat == null) ? 0 : valueFloat.hashCode());
        result = prime * result + ((valueInt == null) ? 0 : valueInt.hashCode());
        result = prime * result + ((valueNumeric == null) ? 0 : valueNumeric.hashCode());
        result = prime * result + ((valueVarchar == null) ? 0 : valueVarchar.hashCode());
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
        TargetParamValDetail other = (TargetParamValDetail) obj;
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
        if (seqNum == null) {
            if (other.seqNum != null)
                return false;
        } else if (!seqNum.equals(other.seqNum))
            return false;
        if (targetParamDefId == null) {
            if (other.targetParamDefId != null)
                return false;
        } else if (!targetParamDefId.equals(other.targetParamDefId))
            return false;
        if (targetParamValDetailId == null) {
            if (other.targetParamValDetailId != null)
                return false;
        } else if (!targetParamValDetailId.equals(other.targetParamValDetailId))
            return false;
        if (valueFloat == null) {
            if (other.valueFloat != null)
                return false;
        } else if (!valueFloat.equals(other.valueFloat))
            return false;
        if (valueInt == null) {
            if (other.valueInt != null)
                return false;
        } else if (!valueInt.equals(other.valueInt))
            return false;
        if (valueNumeric == null) {
            if (other.valueNumeric != null)
                return false;
        } else if (!valueNumeric.equals(other.valueNumeric))
            return false;
        if (valueVarchar == null) {
            if (other.valueVarchar != null)
                return false;
        } else if (!valueVarchar.equals(other.valueVarchar))
            return false;
        return true;
    }

    
    @Override
    public String toString() {
        return "TargetParamValDetail [allocTargetMasterId=" + allocTargetMasterId + ", targetParamValDetailId=" + targetParamValDetailId
                + ", targetParamDefId=" + targetParamDefId + ", allocTargetDetailId=" + allocTargetDetailId + ", valueVarchar=" + valueVarchar
                + ", valueNumeric=" + valueNumeric + ", valueFloat=" + valueFloat + ", valueInt=" + valueInt + ", createdBy=" + createdBy + ", createdTime="
                + createdTime + ", seqNum=" + seqNum + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        out.writeInt(this.targetParamValDetailId);
        out.writeInt(this.allocTargetMasterId);
        out.writeObject(this.targetParamDefId);
        out.writeObject(this.allocTargetDetailId);
        out.writeObject(this.valueVarchar);
        out.writeObject(this.valueNumeric);
        out.writeObject(this.valueFloat);
        out.writeObject(this.valueInt);
        out.writeObject(this.createdBy);
        out.writeObject(this.createdTime);
        out.writeObject(this.seqNum);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
        this.targetParamValDetailId = in.readInt();
        this.allocTargetMasterId = in.readInt();
        this.targetParamDefId = (Integer)in.readObject();
        this.allocTargetDetailId = (Integer)in.readObject();
        this.valueVarchar = (String)in.readObject();
        this.valueNumeric = (Double)in.readObject();
        this.valueFloat = (Double)in.readObject();
        this.valueInt = (Integer)in.readObject();
        this.createdBy = (String)in.readObject();
        this.createdTime = (Date)in.readObject();
        this.seqNum = (Integer)in.readObject();
    }
	
}
