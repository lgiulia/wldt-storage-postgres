package it.wldt.storage.postgres.model.physical;

import it.wldt.storage.model.physical.PhysicalAssetEventNotificationRecord;

import java.util.Map;

public class PostgresPhysicalAssetEventNotificationRecord {
    // Timestamp of the event
    private long timestamp;

    // Event key associated to the event
    private String eventkey;

    // Body of the event
    private Object body;

    // Metadata associated to the event
    private Map<String, Object> metadata;

    // Default constructor
    public PostgresPhysicalAssetEventNotificationRecord() {
    }

    /* Constructors of Physical Asset Event Notification Record with specified details
    @param timestamp  Timestamp of the event
    @param eventkey  Key of the event
    @param body  Body content of the event
    @param metadata  Metadata associated with the event
     */
    public PostgresPhysicalAssetEventNotificationRecord(long timestamp, String eventkey, Object body, Map<String, Object> metadata) {
        this.timestamp = timestamp;
        this.eventkey = eventkey;
        this.body = body;
        this.metadata = metadata;
    }

    // Getter and Setter
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventkey() {
        return eventkey;
    }

    public void setEventkey(String eventkey) {
        this.eventkey = eventkey;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
