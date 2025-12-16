package it.wldt.storage.postgres.model.state;

public class PostgresDigitalTwinStateEventNotification {
    private String digitalEventKey;
    private Object body;
    private Long timestamp;

    // Default constructor
    public PostgresDigitalTwinStateEventNotification() {
    }

    /* Constructs a Digital Twin State Event Notification with the specified event key, body and timestamp
    @param digitalEventKey  the key associated with the digital event
    @param body  the content or payload of the event
    @param timestamp  the time the event was created
     */
    public PostgresDigitalTwinStateEventNotification(String digitalEventKey, Object body, Long timestamp) {
        this.digitalEventKey = digitalEventKey;
        this.body = body;
        this.timestamp = timestamp;
    }

    // Getter and Setter
    public String getDigitalEventKey() {
        return digitalEventKey;
    }

    public void setDigitalEventKey(String digitalEventKey) {
        this.digitalEventKey = digitalEventKey;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
