package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public final class TargetParamValDetailKey implements Externalizable {

    private static final long serialVersionUID = 1L;
    
	private Integer targetParamValDetailId;
	
	@AffinityKeyMapped
    private Integer allocTargetDetailId;

    public TargetParamValDetailKey() {
    }
   
    public TargetParamValDetailKey (Integer targetParamValDetailId, Integer allocTargetDetailId) {
        this.targetParamValDetailId = targetParamValDetailId;
        this.allocTargetDetailId = allocTargetDetailId;
    }

    
    public Integer getTargetParamValDetailId() {
        return targetParamValDetailId;
    }

    public void setTargetParamValDetailId(Integer targetParamValDetailId) {
        this.targetParamValDetailId = targetParamValDetailId;
    }

    public Integer getAllocTargetDetailId() {
        return allocTargetDetailId;
    }

    public void setAllocTargetDetailId(Integer allocTargetDetailId) {
        this.allocTargetDetailId = allocTargetDetailId;
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetDetailId == null) ? 0 : allocTargetDetailId.hashCode());
        result = prime * result + ((targetParamValDetailId == null) ? 0 : targetParamValDetailId.hashCode());
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
        TargetParamValDetailKey other = (TargetParamValDetailKey) obj;
        if (allocTargetDetailId == null) {
            if (other.allocTargetDetailId != null)
                return false;
        } else if (!allocTargetDetailId.equals(other.allocTargetDetailId))
            return false;
        if (targetParamValDetailId == null) {
            if (other.targetParamValDetailId != null)
                return false;
        } else if (!targetParamValDetailId.equals(other.targetParamValDetailId))
            return false;
        return true;
    }

    
    @Override
    public String toString() {
        return "TargetParamValDetailKey [targetParamValDetailId=" + targetParamValDetailId + ", allocTargetDetailId=" + allocTargetDetailId + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.targetParamValDetailId);
        out.writeObject(this.allocTargetDetailId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.targetParamValDetailId = in.readInt();
        this.allocTargetDetailId = (Integer)in.readObject();
    }
	
}
