package it.wldt.storage.postgres.model.lifecycle;

import it.wldt.core.engine.LifeCycleState;

public class PostgresLifeCycleStateRecord {

    // Current lifecycle state
    private LifeCycleState lifeCycleState;

    // Timestamp of the state record
    private long timestamp;

    // Default constructor
    public PostgresLifeCycleStateRecord() {
    }

    /* Constructors of life cycle state record with specified details
    @param lifeCycleState  The lifecycle state to be associated with the record
    @param timestamp  The timestamp of when the lifecycle state was recorded
     */
    public PostgresLifeCycleStateRecord(LifeCycleState lifeCycleState, long timestamp) {
        this.lifeCycleState = lifeCycleState;
        this.timestamp = timestamp;
    }

    // Getter and Setter
    public LifeCycleState getLifeCycleState() {
        return lifeCycleState;
    }

    public void setLifeCycleState(LifeCycleState lifeCycleState) {
        this.lifeCycleState = lifeCycleState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
