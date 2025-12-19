package it.wldt.storage.postgres.service.state;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.state.DigitalTwinStateEventNotificationRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

// Service class for managing Digital Twin State Event Notification records
public class PostgresDigitalTwinStateEventNotificationService {
    private final Connection connection;
    private final ObjectMapper objectMapper;

    // Default constructor
    public PostgresDigitalTwinStateEventNotificationService(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();

        // Jackson configuration
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /* Saves the Digital Twin State Event Notification record
    @param record  The WLDT core record to save
     */
    public void saveRecord(DigitalTwinStateEventNotificationRecord record) throws StorageException {
        // Query sql
        String sql = "INSERT INTO digital_twin_state_event (timestamp, event_key, data) VALUES (?, ?, ?::jsonb)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Timestamp
            ps.setLong(1, record.getTimestamp());
            // Event key
            ps.setString(2, record.getEventKey());
            // Payload
            String jsonPayload = objectMapper.writeValueAsString(record);
            ps.setString(3, jsonPayload);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Saving dt state notification SQL error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Saving dt state notification JSON error: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<DigitalTwinStateEventNotificationRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<DigitalTwinStateEventNotificationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<DigitalTwinStateEventNotificationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
