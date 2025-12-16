package it.wldt.storage.postgres.model.state;

import java.util.List;

import it.wldt.storage.postgres.model.api.PostgresStorageRecord;

// Represents a record of the digital twin's state
public class PostgresDigitalTwinStateRecord extends PostgresStorageRecord {

    // Current Digital Twin State
    private PostgresDigitalTwinState currentState;

    // List of Digital Twin State Changes
    private List<PostgresDigitalTwinStateChange> stateChangeList;

    // Default Constructor
    public PostgresDigitalTwinStateRecord() {
        super();
    }

    /* Constructs a digital twin state record with the specified current state and list of state changes.
    @param currentState  The current state of the digital twin
    @param stateChangeList  List of changes applied to the digital twin's state
     */
    public PostgresDigitalTwinStateRecord(PostgresDigitalTwinState currentState, List<PostgresDigitalTwinStateChange> stateChangeList) {
        this.currentState = currentState;
        this.stateChangeList = stateChangeList;
    }

    // Getter and Setter
    public PostgresDigitalTwinState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PostgresDigitalTwinState currentState) {
        this.currentState = currentState;
    }

    public List<PostgresDigitalTwinStateChange> getStateChangeList() {
        return stateChangeList;
    }

    public void setStateChangeList(List<PostgresDigitalTwinStateChange> stateChangeList) {
        this.stateChangeList = stateChangeList;
    }

    // Return a string representation of the digital twin state record
    @Override
    public String toString() {
        return "DigitalTwinStateRecord {" +
                "id=" + getId() +
                ", currentState=" + currentState +
                ", stateChangeList=" + stateChangeList + "}";
    }
}