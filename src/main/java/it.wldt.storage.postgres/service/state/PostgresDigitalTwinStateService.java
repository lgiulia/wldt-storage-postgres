package it.wldt.storage.postgres.service.state;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.wldt.core.state.DigitalTwinState;
import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.state.DigitalTwinStateRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtTableType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<DigitalTwinStateRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<DigitalTwinStateRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
