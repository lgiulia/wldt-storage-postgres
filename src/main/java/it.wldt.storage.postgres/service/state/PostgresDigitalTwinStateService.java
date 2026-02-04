package it.wldt.storage.postgres.service.state;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.wldt.core.state.DigitalTwinState;
import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.physical.PhysicalAssetEventNotificationRecord;
import it.wldt.storage.model.state.DigitalTwinStateRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtTableType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

// Service class for managing Digital Twin State records
public class PostgresDigitalTwinStateService {
    private final Connection connection;
    private final ObjectMapper objectMapper;

    private static final String TABLE_NAME = PostgresWldtTableType.DIGITAL_TWIN_STATE.getTableName();

    // Default constructor
    public PostgresDigitalTwinStateService(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.objectMapper.registerModule(new Jdk8Module());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /* Saves a DigitalTwinStateRecord
    @param stateRecord  The WLDT record core to save
     */
    public void saveRecord(DigitalTwinStateRecord stateRecord) throws StorageException {
        String sql = "INSERT INTO " + TABLE_NAME + " (timestamp, data) VALUES (?, ?::jsonb)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            long timestamp = System.currentTimeMillis();
            if(stateRecord.getCurrentState() != null && stateRecord.getCurrentState().getEvaluationInstant() != null) {
                timestamp = stateRecord.getCurrentState().getEvaluationInstant().toEpochMilli();
            }

            String jsonPayload = objectMapper.writeValueAsString(stateRecord);

            ps.setLong(1, timestamp);
            ps.setString(2, jsonPayload);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Saving dt state SQL error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Saving dt state SQL error: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<DigitalTwinStateRecord> getLastRecord() throws StorageException {
        String sql = "SELECT data FROM " + TABLE_NAME + " ORDER BY timestamp DESC LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String json = rs.getString("data");
                DigitalTwinStateRecord record = objectMapper.readValue(json, DigitalTwinStateRecord.class);
                return Optional.of(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Digital Twin State SQL error receiving last record: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Digital Twin State JSON processing error receiving last record: " + e.getMessage());
        }
        return Optional.empty();
    }

    public int getRecordsCount() throws StorageException {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new StorageException("Error counting records: " + e.getMessage());
        }
        return 0;
    }

    public List<DigitalTwinStateRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        List<DigitalTwinStateRecord> results = new ArrayList<>();
        String sql = "SELECT data FROM " + TABLE_NAME + " WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, startTimestampMs);
            ps.setLong(2, endTimestampMs);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String json = rs.getString("data");
                    DigitalTwinStateRecord record = objectMapper.readValue(json, DigitalTwinStateRecord.class);
                    results.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Digital Twin State SQL error receiving records in time range: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Digital Twin State JSON processing error receiving records in time range: " + e.getMessage());
        }
        return results;
    }

    public List<DigitalTwinStateRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        List<DigitalTwinStateRecord> results = new ArrayList<>();

        int limit =  endIndex - startIndex; // how many
        int offset = startIndex; // where to start

        String sql = "SELECT data FROM " + TABLE_NAME + " ORDER BY timestamp ASC LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String json = rs.getString("data");
                    DigitalTwinStateRecord record = objectMapper.readValue(json, DigitalTwinStateRecord.class);
                    results.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Digital Twin State SQL error receiving records in range: " + e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new StorageException("Digital Twin State JSON processing error receiving records in range: " + e.getMessage());
        }
        return results;
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        String sql = "SELECT COUNT(*), MIN(timestamp), MAX(timestamp) FROM " + TABLE_NAME;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                long start = rs.getLong(2);
                long end = rs.getLong(3);

                return new StorageStatsRecord(count, start, end);
            }
        } catch (SQLException e) {
            throw new StorageException("Error counting records: " + e.getMessage());
        }
        return new StorageStatsRecord(0, 0, 0);
    }

    public void deleteAllRecords() throws StorageException {
        String sql = "DELETE FROM " + TABLE_NAME;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException("Error deleting all records: " + e.getMessage());
        }
    }
}
