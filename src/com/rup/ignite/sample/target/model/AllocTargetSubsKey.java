package com.rup.ignite.sample.target.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public final class AllocTargetSubsKey implements Externalizable {

    private static final long serialVersionUID = 1L;
    
	private Integer allocTargetSubsId;

	@AffinityKeyMapped
    private Integer allocTargetMasterId;
    
    public AllocTargetSubsKey() {
    }
   
    public AllocTargetSubsKey (Integer allocTargetSubsId, Integer allocTargetMasterId) {

        this.allocTargetSubsId = allocTargetSubsId;
        this.allocTargetMasterId = allocTargetMasterId;
    }

    public Integer getAllocTargetSubsId() {
        return allocTargetSubsId;
    }

    public void setAllocTargetSubsId(Integer allocTargetSubsId) {
        this.allocTargetSubsId = allocTargetSubsId;
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
        result = prime * result + ((allocTargetSubsId == null) ? 0 : allocTargetSubsId.hashCode());
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
        AllocTargetSubsKey other = (AllocTargetSubsKey) obj;
//        if (allocTargetMasterId == null) {
//            if (other.allocTargetMasterId != null)
//                return false;
//        } else if (!allocTargetMasterId.equals(other.allocTargetMasterId))
//            return false;
        if (allocTargetSubsId == null) {
            if (other.allocTargetSubsId != null)
                return false;
        } else if (!allocTargetSubsId.equals(other.allocTargetSubsId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "AllocTargetSubsKey [allocTargetSubsId=" + allocTargetSubsId + ", allocTargetMasterId=" + allocTargetMasterId + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.allocTargetSubsId);
        out.writeInt(this.allocTargetMasterId);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        this.allocTargetSubsId = in.readInt();
        this.allocTargetMasterId = in.readInt();
    }
	
}
