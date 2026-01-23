package it.wldt.storage.postgres.service.physical;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.physical.PhysicalAssetActionRequestRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtTableType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;

// Service class to manage Physical Asset Action Requests records in PostgreSQL
public class PostgresPhysicalAssetActionRequestService {

    private final Connection connection;
    private final ObjectMapper objectMapper;

    private static final String TABLE_NAME = PostgresWldtTableType.PHYSICAL_ACTION_REQUEST.getTableName();

    // Default Constructor
    public PostgresPhysicalAssetActionRequestService(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();

        // Jackson configuration
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /* Saves a Physical Asset Action Request Service record
    @param record  Generic WLDT record to save
     */
    public void saveRecord(PhysicalAssetActionRequestRecord record) throws StorageException {
        // SQL query
        String sql = "INSERT INTO " + TABLE_NAME + " (timestamp, action_key, data) VALUES (?, ?, ?::jsonb)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Timestamp
            ps.setLong(1, record.getRequestTimestamp());
            // Action key
            ps.setString(2, record.getActionkey());
            // Payload
            String jsonPayload = objectMapper.writeValueAsString(record);
            ps.setString(3, jsonPayload);
            // Execute
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Saving physical action request SQL error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Saving physical action request JSON error: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<PhysicalAssetActionRequestRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetActionRequestRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetActionRequestRecord> getRecordsInRange(int startTimestampMs, int endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}