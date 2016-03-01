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

@AladdinDatabaseObject(getTbl="alloc_target_strategy", keyClass=Integer.class)
public final class AllocTargetStrategy extends AbstractAladdinEntity<Integer> implements Externalizable {

    private static final long serialVersionUID = 1L;
    
    @AladdinColumn(columnName="alloc_target_strategy_id", columnType=ColumnType.INT, primaryKey=true)
	@QuerySqlField(name="alloc_target_strategy_id",index = true)
	private Integer allocTargetStrategyId;
	
    @AladdinColumn(columnName="description", columnType=ColumnType.STRING, length=255)
    @QuerySqlField(name="description")
	private String description;
	
    @AladdinColumn(columnName="setup_app", columnType=ColumnType.STRING, length=15)
	@QuerySqlField(name="setup_app")
	private String setupApp;

    @AladdinColumn(columnName="processing_app", columnType=ColumnType.STRING, length=15)
	@QuerySqlField(name="processing_app")
	private String processingApp;

    @AladdinColumn(columnName="active", columnType=ColumnType.STRING, length=1)
    @QuerySqlField(name="active")
	private String active;
	
    @AladdinColumn(columnName="created_by", columnType=ColumnType.STRING, length=8)
    @QuerySqlField(name="created_by")
	private String createdBy;
	
    @AladdinColumn(columnName="created_time", columnType=ColumnType.DATE)
    @QuerySqlField(name="created_time")
	private Date createdTime;
	
    
    public static AladdinSybaseCache<Integer, AllocTargetStrategy> createCache(AladdinCacheManager acm, String name, Coldboot coldboot)
    {
        return acm.createSybaseCache(name, Integer.class, AllocTargetStrategy.class, coldboot, CacheWriteSynchronizationMode.FULL_ASYNC, CacheRebalanceMode.ASYNC, 0, CacheMode.REPLICATED, CacheAtomicityMode.TRANSACTIONAL);
    }
    
    public AllocTargetStrategy() {
        super(null);
    }
   
    public AllocTargetStrategy (Integer allocTargetStrategyId, String description, String setupApp, String processingApp, String active, String createdBy, Date createdTime) {
        super(new Integer(allocTargetStrategyId));

        this.allocTargetStrategyId = allocTargetStrategyId;
        this.description = description;
        this.setupApp = setupApp;
        this.processingApp = processingApp;
        this.active = active;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
    }


    public Integer getAllocTargetStrategyId() {
        return allocTargetStrategyId;
    }

    public void setAllocTargetStrategyId(Integer allocTargetStrategyId) {
        this.allocTargetStrategyId = allocTargetStrategyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSetupApp() {
        return setupApp;
    }

    public void setSetupApp(String setupApp) {
        this.setupApp = setupApp;
    }

    public String getProcessingApp() {
        return processingApp;
    }

    public void setProcessingApp(String processingApp) {
        this.processingApp = processingApp;
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
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((processingApp == null) ? 0 : processingApp.hashCode());
        result = prime * result + ((setupApp == null) ? 0 : setupApp.hashCode());
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
        AllocTargetStrategy other = (AllocTargetStrategy) obj;
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
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (processingApp == null) {
            if (other.processingApp != null)
                return false;
        } else if (!processingApp.equals(other.processingApp))
            return false;
        if (setupApp == null) {
            if (other.setupApp != null)
                return false;
        } else if (!setupApp.equals(other.setupApp))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AllocTargetStrategy [allocTargetStrategyId=" + allocTargetStrategyId + ", description=" + description + ", setupApp=" + setupApp
                + ", processingApp=" + processingApp + ", active=" + active + ", createdBy=" + createdBy + ", createdTime=" + createdTime + "]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        out.writeInt(this.allocTargetStrategyId);
        out.writeObject(this.description);
        out.writeObject(this.setupApp);
        out.writeObject(this.processingApp);
        out.writeObject(this.active);
        out.writeObject(this.createdBy);
        out.writeObject(this.createdTime);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
        this.allocTargetStrategyId = in.readInt();
        this.description = (String)in.readObject();
        this.setupApp = (String)in.readObject();
        this.processingApp = (String)in.readObject();
        this.active = (String)in.readObject();
        this.createdBy = (String)in.readObject();
        this.createdTime = (Date)in.readObject();
    }
	
}
