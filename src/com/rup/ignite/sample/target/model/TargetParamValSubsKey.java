package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public final class TargetParamValSubsKey implements Externalizable {

    private Integer targetParamValSubsId;

    @AffinityKeyMapped
    private Integer allocTargetSubsId;

    public TargetParamValSubsKey() {
    }
   
    public TargetParamValSubsKey (Integer targetParamValSubsId, Integer allocTargetSubsId) {
        this.targetParamValSubsId = targetParamValSubsId;
        this.allocTargetSubsId = allocTargetSubsId;
    }

    
    public Integer getTargetParamValSubsId() {
        return targetParamValSubsId;
    }

    public void setTargetParamValSubsId(Integer targetParamValSubsId) {
        this.targetParamValSubsId = targetParamValSubsId;
    }

    public Integer getAllocTargetSubsId() {
        return allocTargetSubsId;
    }

    public void setAllocTargetSubsId(Integer allocTargetSubsId) {
        this.allocTargetSubsId = allocTargetSubsId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetSubsId == null) ? 0 : allocTargetSubsId.hashCode());
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
        if (allocTargetSubsId == null) {
            if (other.allocTargetSubsId != null)
                return false;
        } else if (!allocTargetSubsId.equals(other.allocTargetSubsId))
            return false;
        if (targetParamValSubsId == null) {
            if (other.targetParamValSubsId != null)
                return false;
        } else if (!targetParamValSubsId.equals(other.targetParamValSubsId))
            return false;
        return true;
    }

    
    @Override
    public String toString() {
        return "TargetParamValSubsKey [targetParamValSubsId=" + targetParamValSubsId + ", allocTargetSubsId=" + allocTargetSubsId + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.targetParamValSubsId);
        out.writeObject(this.allocTargetSubsId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.targetParamValSubsId = in.readInt();
        this.allocTargetSubsId = (Integer)in.readObject();
    }
	
}
