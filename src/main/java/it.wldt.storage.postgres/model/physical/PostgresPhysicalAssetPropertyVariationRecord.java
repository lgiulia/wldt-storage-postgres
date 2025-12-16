package it.wldt.storage.postgres.model.physical;

import java.util.Map;

public class PostgresPhysicalAssetPropertyVariationRecord {
    // Timestamp of the variation
    private long timestamp;

    // Property key associated to the variation
    private String propertykey;

    // Body of the variation
    private Object body;

    // Metadata associated to the variation
    private Map<String, Object> variationMetadata;

    // Default constructor
    public PostgresPhysicalAssetPropertyVariationRecord() {
        super();
    }

    /* Constructors of physical asset property variation record with specified details
    @param timestamp  Timestamp of the property variation
    @param propertykey  Key of the property associated with the variation
    @param body  Body content of the variation
    @param variationMetadata  Metadata associated with the property variation
     */
    public PostgresPhysicalAssetPropertyVariationRecord(long timestamp, String propertykey, Object body, Map<String, Object> variationMetadata) {
        this.timestamp = timestamp;
        this.propertykey = propertykey;
        this.body = body;
        this.variationMetadata = variationMetadata;
    }

    // Getter and Setter

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPropertykey() {
        return propertykey;
    }

    public void setPropertykey(String propertykey) {
        this.propertykey = propertykey;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, Object> getVariationMetadata() {
        return variationMetadata;
    }

    public void setVariationMetadata(Map<String, Object> variationMetadata) {
        this.variationMetadata = variationMetadata;
    }
}
