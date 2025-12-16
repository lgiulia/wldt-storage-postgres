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
import it.wldt.storage.model.physical.PhysicalAssetEventNotificationRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Service class for managing Physical Asset Event Notification records
public class PostgresPhysicalAssetEventNotificationService {
    private final Connection connection;
    private final ObjectMapper objectMapper;
    // Default constructor
    public PostgresPhysicalAssetEventNotificationService(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();

        // Jackson Configuration
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.objectMapper.registerModule(new Jdk8Module());
    }

    /* Saves a PhysicalAssetEventNotificationRecord
    @param record Record of the WLDT core to save
     */
    public void saveRecord(PhysicalAssetEventNotificationRecord record) throws StorageException {
        String sql = "INSERT INTO physical_asset_event (timestamp, event_key, data) VALUES (?, ?, ?::jsonb)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, record.getTimestamp());
            ps.setString(2, record.getEventkey());
            String jsonPayload = objectMapper.writeValueAsString(record);
            ps.setString(3, jsonPayload);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Physical event SQL error saving: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Physical event JSON processing error saving: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<PhysicalAssetEventNotificationRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetEventNotificationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetEventNotificationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
