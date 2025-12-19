package it.wldt.storage.postgres.service.physical;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.wldt.adapter.physical.PhysicalAssetPropertyVariation;
import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.physical.PhysicalAssetPropertyVariationRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Service class for managing Physical Asset Property Variation records
public class PostgresPhysicalAssetPropertyVariationService {
    private final Connection connection;
    private final ObjectMapper objectMapper;

    // Default constructor
    public PostgresPhysicalAssetPropertyVariationService(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /* Saves a PhysicalAssetPropertyVariationRecord
    @param record  WLDT core record to save
     */
    public void saveRecord(PhysicalAssetPropertyVariationRecord record) throws StorageException {
        // Query SQL
        String sql = "INSERT INTO physical_asset_property_variation (timestamp, property_key, data) VALUES (?, ?, ?::jsonb)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Timestamp
            ps.setLong(1, record.getTimestamp());
            // porperty_key
            ps.setString(2, record.getPropertykey());
            // Payload
            String jsonPayload = objectMapper.writeValueAsString(record);
            ps.setString(3, jsonPayload);
            // Execute
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Saving physical asset property variation SQL error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Saving physical asset property variation JSON error: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<PhysicalAssetPropertyVariationRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetPropertyVariationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetPropertyVariationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
