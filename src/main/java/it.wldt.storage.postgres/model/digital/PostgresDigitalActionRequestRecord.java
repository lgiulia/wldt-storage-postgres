package it.wldt.storage.postgres.model.digital;
import java.util.Map;

public class PostgresDigitalActionRequestRecord {

    // Timestamp of the request
    private long requestTimestamp;

    // Action Key associated to the request
    private String actionKey;

    // Request Body
    private Object requestBody;

    // Metadata associated to the request
    private Map<String, Object> requestMetadata;

    // Default Constructor
    public PostgresDigitalActionRequestRecord() {
    }

    /* Constructors of digital action request record with details
    @param requestTimestamp  The timestamp of the record
    @param actionKey  The action key associated with the request
    @param requestBody  The body of the request
    @param requestMetadata  The metadata associated with the request
     */
    public PostgresDigitalActionRequestRecord(long requestTimestamp, String actionKey, Object requestBody, Map<String, Object> requestMetadata) {
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
