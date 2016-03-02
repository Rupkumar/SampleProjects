package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public final class AllocTargetMappingKey implements Externalizable {

    private static final long serialVersionUID = 1L;
    
	private Integer allocTargetMappingId;

	@AffinityKeyMapped
	private Integer allocTargetDetailId;
	
    public AllocTargetMappingKey() {
    }
   
    public AllocTargetMappingKey (Integer allocTargetMappingId, Integer allocTargetDetailId) {
        this.allocTargetMappingId = allocTargetMappingId;
        this.allocTargetDetailId = allocTargetDetailId;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allocTargetDetailId == null) ? 0 : allocTargetDetailId.hashCode());
        result = prime * result + ((allocTargetMappingId == null) ? 0 : allocTargetMappingId.hashCode());
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
        AllocTargetMappingKey other = (AllocTargetMappingKey) obj;
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
        return true;
    }

    @Override
    public String toString() {
        return "AllocTargetMappingKey [allocTargetMappingId=" + allocTargetMappingId + ", allocTargetDetailId=" + allocTargetDetailId + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt( this.allocTargetMappingId);
        out.writeInt(this.allocTargetDetailId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.allocTargetMappingId = in.readInt();
        this.allocTargetDetailId = in.readInt();
    }
	
}
