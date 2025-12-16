package it.wldt.storage.postgres.model.state;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import it.wldt.core.state.DigitalTwinState;
import it.wldt.core.state.DigitalTwinStateAction;
import it.wldt.core.state.DigitalTwinStateEvent;
import it.wldt.core.state.DigitalTwinStateProperty;
import it.wldt.core.state.DigitalTwinStateRelationship;
import it.wldt.exception.WldtDigitalTwinStateActionException;
import it.wldt.exception.WldtDigitalTwinStateEventException;
import it.wldt.exception.WldtDigitalTwinStatePropertyException;

public class PostgresDigitalTwinState {
    private List<DigitalTwinStateAction> actionList;
    private List<DigitalTwinStateProperty<?>> propertyList;
    private List<DigitalTwinStateRelationship<?>> relationshipList;
    private List<DigitalTwinStateEvent> eventList;
    private Instant evaluationInstant;

    // Default constructor
    public PostgresDigitalTwinState() {
    }

    // Constructs an instance with specified lists of actions, properties, relationships and events
    public PostgresDigitalTwinState(List<DigitalTwinStateAction> action, List<DigitalTwinStateProperty<?>> property, List<DigitalTwinStateRelationship<?>> relationship, List<DigitalTwinStateEvent> event, Instant evaluationInstant) {
        this.actionList = action;
        this.propertyList = property;
        this.relationshipList = relationship;
        this.eventList = event;
        this.evaluationInstant = evaluationInstant;
    }

    /* Constructs an instance based on an existing Digital Twin
    @param digitalTwinState  the DigitalTwinState instance to copy data from
    @throws WldtDigitalTwinStateActionException  if an error occurs while retrieving actions
    @throws WldtDigitalTwinStatePropertyException  if an error occurs while retrieving properties
    @throws WldtDigitalTwinStateEventException  if an error occurs while retrieving events
     */
    public PostgresDigitalTwinState(DigitalTwinState digitalTwinState)
            throws WldtDigitalTwinStateActionException, WldtDigitalTwinStateActionException, WldtDigitalTwinStatePropertyException, WldtDigitalTwinStateEventException {
        this.actionList = digitalTwinState.getActionList().isPresent() ? digitalTwinState.getActionList().get() : new ArrayList<>();
        this.propertyList = digitalTwinState.getPropertyList().isPresent() ? digitalTwinState.getPropertyList().get() : new ArrayList<>();
        this.relationshipList = digitalTwinState.getRelationshipList().isPresent() ? digitalTwinState.getRelationshipList().get() : new ArrayList<>();
        this.eventList = digitalTwinState.getEventList().isPresent() ? digitalTwinState.getEventList().get() : new ArrayList<>();
        this.evaluationInstant = digitalTwinState.getEvaluationInstant();
    }

    // Getter and Setter

    public List<DigitalTwinStateAction> getActionList() {
        return actionList;
    }

    public void setActionList(List<DigitalTwinStateAction> actionList) {
        this.actionList = actionList;
    }

    public List<DigitalTwinStateProperty<?>> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<DigitalTwinStateProperty<?>> propertyList) {
        this.propertyList = propertyList;
    }

    public List<DigitalTwinStateRelationship<?>> getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(List<DigitalTwinStateRelationship<?>> relationshipList) {
        this.relationshipList = relationshipList;
    }

    public List<DigitalTwinStateEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<DigitalTwinStateEvent> eventList) {
        this.eventList = eventList;
    }

    public Instant getEvaluationInstant() {
        return evaluationInstant;
    }

    public void setEvaluationInstant(Instant evaluationInstant) {
        this.evaluationInstant = evaluationInstant;
    }
}
