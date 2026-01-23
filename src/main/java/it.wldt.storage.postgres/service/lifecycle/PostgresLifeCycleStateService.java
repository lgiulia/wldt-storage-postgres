package it.wldt.storage.postgres.service.lifecycle;

import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.lifecycle.LifeCycleVariationRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtTableType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Service class for managing Life Cycle State records in PostgreSQL
public class PostgresLifeCycleStateService {
    private final Connection connection;

    private static final String TABLE_NAME = PostgresWldtTableType.LIFE_CYCLE_STATE_VARIATION.getTableName();

    // Constructor
    public PostgresLifeCycleStateService(Connection connection) {
        this.connection = connection;
    }

    /* Saves a LifeCycleVariationRecord to the database
    @param record  the record to save
     */
    public void saveRecord(LifeCycleVariationRecord record) throws StorageException {
        String sql = "INSERT INTO " + TABLE_NAME + " (timestamp, state) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            // Timestamp
            ps.setLong(1, record.getTimestamp());
            // State
            ps.setString(2, record.getLifeCycleState().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Saving Life Cycle State SQL error: " + e.getMessage());
        }
    }

    // Methods to access data
    public Optional<LifeCycleVariationRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int  getRecordCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<LifeCycleVariationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<LifeCycleVariationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}