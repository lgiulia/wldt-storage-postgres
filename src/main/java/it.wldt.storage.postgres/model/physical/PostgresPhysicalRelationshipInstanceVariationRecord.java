package it.wldt.storage.postgres.model.physical;

import it.wldt.storage.model.physical.PhysicalRelationshipInstanceVariationRecord;

import java.util.HashMap;
import java.util.Map;

public class PostgresPhysicalRelationshipInstanceVariationRecord {
    // Timestamp of the variation
    private long variationTimestamp;

    // Instance Key associated to Relationship Instance Variation
    private String instanceKey;

    // Instance Target ID associated to Relationship Instance Variation
    private Object instanceTargetId;

    // Relationship Name associated to Relationship Instance Variation
    private String relationshipName;

    // Relationship Type associated to Relationship Instance Variation
    private String relationshipType;

    // Metadata
    private final Map<String, Object> metadata = new HashMap<>();

    // Default Constructor
    public PostgresPhysicalRelationshipInstanceVariationRecord() {
    }

    /* Constructors of physical relationship instance variation record with specified details
    @param variationTimestamp  Timestamp of the relationship instance variation
    @param instanceKey  Key of the instance associated with the variation
    @param instanceTargetId  Target ID of the relationship instance variation
    @param relationshipName  Name of the relationship associated with the variation
    @param relationshipType  Type of the relationship associated with the variation
    @param metadata  Additional metadata associated with the variation
     */
    public PostgresPhysicalRelationshipInstanceVariationRecord(long variationTimestamp, String instanceKey, Object instanceTargetId, String relationshipName, String relationshipType, Map<String, Object> metadata) {
        this.variationTimestamp = variationTimestamp;
        this.instanceKey = instanceKey;
        this.instanceTargetId = instanceTargetId;
        this.relationshipName = relationshipName;
        this.relationshipType = relationshipType;
        this.metadata.putAll(metadata);
    }

    // Getter and Setter

    public long getVariationTimestamp() {
        return variationTimestamp;
    }

    public void setVariationTimestamp(long variationTimestamp) {
        this.variationTimestamp = variationTimestamp;
    }

    public String getInstanceKey() {
        return instanceKey;
    }

    public void setInstanceKey(String instanceKey) {
        this.instanceKey = instanceKey;
    }

    public Object getInstanceTargetId() {
        return instanceTargetId;
    }

    public void setInstanceTargetId(Object instanceTargetId) {
        this.instanceTargetId = instanceTargetId;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}