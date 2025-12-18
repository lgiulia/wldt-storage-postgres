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
import it.wldt.storage.model.physical.PhysicalRelationshipInstanceVariationRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

// Service class for managing Physical Relationship Instance Variation records
public class PostgresPhysicalRelationshipInstanceVariationService {
    private final Connection connection;
    private final ObjectMapper objectMapper;

    // Default constructor
    public PostgresPhysicalRelationshipInstanceVariationService(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /* Saves a PhysicalRelationshipInstanceVariationRecord
    @param record  The WLDT core record to save
     */
    public void saveRecord(PhysicalRelationshipInstanceVariationRecord record) throws StorageException {
        String sql = "INSERT INTO physical_relationship_instance_variation (timestamp, instance_key, instance_target_id, relationship_name, relationship_type, data) VALUES (?, ?, ?, ?, ?, ?::jsonb)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Timestamp
            ps.setLong(1, record.getVariationTimestamp());
            // Instance key
            ps.setString(2, record.getInstanceKey());
            // Target ID
            if (record.getInstanceTargetId() != null) {
                ps.setString(3, record.getInstanceTargetId().toString());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }
            // Relationship name
            ps.setString(4, record.getRelationshipName());
            // Relationship type
            ps.setString(5, record.getRelationshipType());
            // Payload
            String jsonPayload = objectMapper.writeValueAsString(record);
            ps.setString(6, jsonPayload);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Saving action request SQL error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Saving action request JSON error: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<PhysicalRelationshipInstanceVariationRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalRelationshipInstanceVariationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalRelationshipInstanceVariationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
