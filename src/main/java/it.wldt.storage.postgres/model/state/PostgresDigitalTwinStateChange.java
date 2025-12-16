package it.wldt.storage.postgres.model.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import it.wldt.core.state.DigitalTwinStateAction;
import it.wldt.core.state.DigitalTwinStateEvent;
import it.wldt.core.state.DigitalTwinStateChange.Operation;
import it.wldt.core.state.DigitalTwinStateChange.ResourceType;
import it.wldt.core.state.DigitalTwinStateProperty;
import it.wldt.core.state.DigitalTwinStateRelationship;
import it.wldt.core.state.DigitalTwinStateResource;

// Represents a change in the state of a digital twin
public class PostgresDigitalTwinStateChange {
    private Operation operation;
    private ResourceType resourceType;

    // The Digital Twin resource affected by this change
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DigitalTwinStateProperty.class),
            @JsonSubTypes.Type(value = DigitalTwinStateAction.class),
            @JsonSubTypes.Type(value = DigitalTwinStateRelationship.class),
            @JsonSubTypes.Type(value = DigitalTwinStateResource.class),
    })
    private DigitalTwinStateResource resource;

    // Default constructor
    public PostgresDigitalTwinStateChange() {
    }

    /* Constructs an instance with specified operation, resource type and affected resource
    @param operation  the operation representing the type of change (ADD, UPDATE, DELETE)
    @param resourceType  the resource type of the resource affected by this change
    @param resource  the Digital Twin resource instance that has been changed
     */
    public PostgresDigitalTwinStateChange(Operation operation, ResourceType resourceType, DigitalTwinStateResource resource) {
        this.operation = operation;
        this.resourceType = resourceType;
        this.resource = resource;
    }

    // Getter and Setter
    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public DigitalTwinStateResource getResource() {
        return resource;
    }

    public void setResource(DigitalTwinStateResource resource) {
        this.resource = resource;
    }
}
