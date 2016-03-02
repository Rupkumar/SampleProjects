package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public final class AllocTargetDetailKey implements Externalizable {

    private static final long serialVersionUID = 1L;
    
	private Integer allocTargetDetailId;

	@AffinityKeyMapped
	private Integer allocTargetMasterId;
	
    public AllocTargetDetailKey() {
    }
   
    public AllocTargetDetailKey (Integer allocTargetDetailId, Integer allocTargetMasterId) {
        this.allocTargetDetailId = allocTargetDetailId;
        this.allocTargetMasterId = allocTargetMasterId;
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

    
    @Override
    public String toString() {
        return "AllocTargetDetailKey [allocTargetDetailId=" + allocTargetDetailId + ", allocTargetMasterId=" + allocTargetMasterId + "]";
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetDetailId == null) ? 0 : allocTargetDetailId.hashCode());
        result = prime * result + ((allocTargetMasterId == null) ? 0 : allocTargetMasterId.hashCode());
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
        AllocTargetDetailKey other = (AllocTargetDetailKey) obj;
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
        return true;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.allocTargetDetailId);
        out.writeInt(this.allocTargetMasterId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.allocTargetDetailId = in.readInt();
        this.allocTargetMasterId = in.readInt();
    }
	
}
