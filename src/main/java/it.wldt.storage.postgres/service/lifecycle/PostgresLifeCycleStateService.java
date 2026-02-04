package it.wldt.storage.postgres.service.lifecycle;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.wldt.core.engine.LifeCycleState;
import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.lifecycle.LifeCycleVariationRecord;
import it.wldt.storage.model.physical.PhysicalAssetEventNotificationRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtTableType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public LifeCycleVariationRecord getLastRecord() throws StorageException {
        String sql = "SELECT state, timestamp FROM " + TABLE_NAME + " ORDER BY timestamp DESC LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToRecord(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new StorageException("Lifecycle variation SQL error receiving last record: " + e.getMessage());
        }
        return null;
    }

    public int  getRecordCount() throws StorageException {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new StorageException("Error counting Lifecycle records: " + e.getMessage());
        }
        return 0;
    }

    public List<LifeCycleVariationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        List<LifeCycleVariationRecord> results = new ArrayList<>();
        String sql = "SELECT state, timestamp FROM " + TABLE_NAME + " WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, startTimestampMs);
            ps.setLong(2, endTimestampMs);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new StorageException("Error reading Lifecycle time range: " + e.getMessage());
        }
        return results;
    }

    public List<LifeCycleVariationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        List<LifeCycleVariationRecord> results = new ArrayList<>();
        int limit = endIndex - startIndex;
        int offset = startIndex;
        String sql = "SELECT state, timestamp FROM " + TABLE_NAME + " ORDER BY timestamp ASC LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new StorageException("Error reading Lifecycle range: " + e.getMessage());
        }
        return results;
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        String sql = "SELECT COUNT(*), MIN(timestamp), MAX(timestamp) FROM " + TABLE_NAME;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new StorageStatsRecord(rs.getInt(1), rs.getLong(2), rs.getLong(3));
            }
        } catch (SQLException e) {
            throw new StorageException("Error stats Lifecycle: " + e.getMessage());
        }
        return new StorageStatsRecord(0, 0, 0);
    }

    public void deleteAllRecords() throws StorageException {
        String sql = "DELETE FROM " + TABLE_NAME;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException("Error deleting Lifecycle records: " + e.getMessage());
        }
    }

    private LifeCycleVariationRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        String stateString = rs.getString("state");
        long timestamp = rs.getLong("timestamp");
        LifeCycleState state = LifeCycleState.valueOf(stateString);
        return new LifeCycleVariationRecord(state, timestamp);
    }
}