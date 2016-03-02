package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public final class TargetParamValMasterKey implements Externalizable {

    private static final long serialVersionUID = 1L;
    
	private Integer targetParamValMasterId;
	
	@AffinityKeyMapped
    private Integer allocTargetMasterId;
    
    public TargetParamValMasterKey() {
    }
   
    public TargetParamValMasterKey (Integer targetParamValMasterId, Integer allocTargetMasterId) {
        this.targetParamValMasterId = targetParamValMasterId;
        this.allocTargetMasterId = allocTargetMasterId;
    }

    public Integer getTargetParamValMasterId() {
        return targetParamValMasterId;
    }

    public void setTargetParamValMasterId(Integer targetParamValMasterId) {
        this.targetParamValMasterId = targetParamValMasterId;
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
        result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
        result = prime * result + ((targetParamValMasterId == null) ? 0 : targetParamValMasterId.hashCode());
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
        TargetParamValMasterKey other = (TargetParamValMasterKey) obj;
        if (allocTargetMasterId == null) {
            if (other.allocTargetMasterId != null)
                return false;
        } else if (!allocTargetMasterId.equals(other.allocTargetMasterId))
            return false;
        if (targetParamValMasterId == null) {
            if (other.targetParamValMasterId != null)
                return false;
        } else if (!targetParamValMasterId.equals(other.targetParamValMasterId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "TargetParamValMasterKey [targetParamValMasterId=" + targetParamValMasterId + ", allocTargetMasterId=" + allocTargetMasterId + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.targetParamValMasterId);
        out.writeObject(this.allocTargetMasterId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.targetParamValMasterId = in.readInt();
        this.allocTargetMasterId = (Integer)in.readObject();
    }
	
}
