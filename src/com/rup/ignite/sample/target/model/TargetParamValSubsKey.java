package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public final class TargetParamValSubsKey implements Externalizable {

    private Integer targetParamValSubsId;

    @AffinityKeyMapped
    private Integer allocTargetMasterId;

    public TargetParamValSubsKey() {
    }
   
    public TargetParamValSubsKey (Integer targetParamValSubsId, Integer allocTargetMasterId) {
        this.targetParamValSubsId = targetParamValSubsId;
        this.allocTargetMasterId = allocTargetMasterId;
    }

    
    public Integer getTargetParamValSubsId() {
        return targetParamValSubsId;
    }

    public void setTargetParamValSubsId(Integer targetParamValSubsId) {
        this.targetParamValSubsId = targetParamValSubsId;
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
        //result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
        result = prime * result + ((targetParamValSubsId == null) ? 0 : targetParamValSubsId.hashCode());
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
        TargetParamValSubsKey other = (TargetParamValSubsKey) obj;
//        if (allocTargetMasterId == null) {
//            if (other.allocTargetMasterId != null)
//                return false;
//        } else if (!allocTargetMasterId.equals(other.allocTargetMasterId))
//            return false;
        if (targetParamValSubsId == null) {
            if (other.targetParamValSubsId != null)
                return false;
        } else if (!targetParamValSubsId.equals(other.targetParamValSubsId))
            return false;
        return true;
    }

    
    @Override
    public String toString() {
        return "TargetParamValSubsKey [targetParamValSubsId=" + targetParamValSubsId + ", allocTargetSubsId=" + allocTargetMasterId + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.targetParamValSubsId);
        out.writeObject(this.allocTargetMasterId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.targetParamValSubsId = in.readInt();
        this.allocTargetMasterId = (Integer)in.readObject();
    }
	
}
