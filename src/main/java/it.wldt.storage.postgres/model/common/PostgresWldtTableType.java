package it.wldt.storage.postgres.model.common;

// Enum representing the various types of table for postgres
public enum PostgresWldtTableType {
    DIGITAL_TWIN_STATE("digital_twin_state"),
    DIGITAL_TWIN_STATE_EVENT_NOTIFICATION("digital_twin_state_event"),
    LIFE_CYCLE_STATE_VARIATION("lifecycle_state"),
    PHYSICAL_ASSET_EVENT_NOTIFICATION("physical_asset_event"),
    PHYSICAL_ACTION_REQUEST("physical_asset_action_request"),
    DIGITAL_ACTION_REQUEST("digital_action_request"),
    NEW_PHYSICAL_ASSET_DESCRIPTION_NOTIFICATION("physical_asset_description"),
    UPDATED_PHYSICAL_ASSET_DESCRIPTION_NOTIFICATION("physical_asset_description"),
    PHYSICAL_ASSET_PROPERTY_VARIATION("physical_asset_property_variation"),
    PHYSICAL_ASSET_RELATIONSHIP_INSTANCE_CREATED_NOTIFICATION("physical_relationship_instance_variation"),
    PHYSICAL_ASSET_RELATIONSHIP_INSTANCE_DELETED_NOTIFICATION("physical_relationship_instance_variation"),;

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
