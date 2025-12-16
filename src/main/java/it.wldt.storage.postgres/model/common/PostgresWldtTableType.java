package it.wldt.storage.postgres.model.common;

// Enum representing the various types of table for postgres
public enum PostgresWldtTableType {
    DIGITAL_TWIN_STATE("DigitalTwinState"),
    DIGITAL_TWIN_STATE_EVENT_NOTIFICATION("DigitalTwinStateEventNotification"),
    LIFE_CYCLE_STATE_VARIATION("LifeCycleStateVariation"),
    PHYSICAL_ASSET_EVENT_NOTIFICATION("PhysicalAssetEventNotification"),
    PHYSICAL_ACTION_REQUEST("PhysicalActionRequest"),
    DIGITAL_ACTION_REQUEST("DigitalActionRequest"),
    NEW_PHYSICAL_ASSET_DESCRIPTION_NOTIFICATION("NewPhysicalAssetDescriptionNotification"),
    UPDATED_PHYSICAL_ASSET_DESCRIPTION_NOTIFICATION("UpdatedPhysicalAssetDescriptionNotification"),
    PHYSICAL_ASSET_PROPERTY_VARIATION("PhysicalAssetPropertyVariation"),
    PHYSICAL_ASSET_RELATIONSHIP_INSTANCE_CREATED_NOTIFICATION("PhysicalAssetRelationshipInstanceCreatedNotification"),
    PHYSICAL_ASSET_RELATIONSHIP_INSTANCE_DELETED_NOTIFICATION("PhysicalAssetRelationshipInstanceDeletedNotification");

    // The name of the table associated with the enumeration constant
    private final String tableName;

    // Constructor for PostgresWldtTableType
    PostgresWldtTableType(String tableName) {
        this.tableName = tableName;
    }

    // Getter
    public String getTableName() {
        return tableName;
    }
}
