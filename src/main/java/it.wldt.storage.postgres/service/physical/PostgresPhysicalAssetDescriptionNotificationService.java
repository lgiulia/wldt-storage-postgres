package it.wldt.storage.postgres.service.physical;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.wldt.adapter.physical.PhysicalAssetDescriptionNotification;
import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.physical.PhysicalAssetDescriptionNotificationRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtTableType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Service class for managing Physical Asset Description Notification
public class PostgresPhysicalAssetDescriptionNotificationService {
    private final Connection connection;
    private final ObjectMapper objectMapper;

    private static final String TABLE_NAME = PostgresWldtTableType.NEW_PHYSICAL_ASSET_DESCRIPTION_NOTIFICATION.getTableName();

    // Default constructor
    public PostgresPhysicalAssetDescriptionNotificationService(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /* Save a record of Physical Asset Description Notification Record
    @param record  Record core WLDT to save
     */
    public void saveRecord(PhysicalAssetDescriptionNotificationRecord record) throws StorageException {
        String sql = "INSERT INTO " + TABLE_NAME + " (timestamp, adapter_id, data) VALUES (?, ?, ?::jsonb)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, record.getNotificationTimestamp());
            ps.setString(2, record.getAdapterId());
            String jsonPayload = objectMapper.writeValueAsString(record);
            ps.setString(3, jsonPayload);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Saving physical asset description notification SQL error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Saving physical asset description notification JSON error: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<PhysicalAssetDescriptionNotificationRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetDescriptionNotificationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalAssetDescriptionNotificationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords()  throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
