package it.wldt.storage.postgres.model.physical;

import java.util.Map;

public class PostgresPhysicalAssetActionRequestRecord {
    // Timestamp of the request
    private long requestTimestamp;

    // Action key associated with the request
    private String actionKey;

    // Body of the request
    private Object requestBody;

    // Metadata associated with the request
    private Map<String, Object> requestMetadata;

    // Default Constructor
    public PostgresPhysicalAssetActionRequestRecord() {
    }

    /* Constructors of physical asset action request record with specified details
        @param requestTimestamp  Timestamp of the request
        @param actionKey  Key of the action associated with the request
        @param requestBody  Body of the request
        @param requestMetadata  Metadata associated with the request
     */
    public PostgresPhysicalAssetActionRequestRecord(long requestTimestamp, String actionKey, Object requestBody, Map<String, Object> requestMetadata) {
        this.requestTimestamp = requestTimestamp;
        this.actionKey = actionKey;
        this.requestBody = requestBody;
        this.requestMetadata = requestMetadata;
    }

    // Getter and Setter

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public String getActionKey() {
        return actionKey;
    }

    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, Object> getRequestMetadata() {
        return requestMetadata;
    }

    public void setRequestMetadata(Map<String, Object> requestMetadata) {
        this.requestMetadata = requestMetadata;
    }
}
