package it.wldt.storage.postgres.service.state;

import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.state.DigitalTwinStateEventNotificationRecord;

import java.util.List;
import java.util.Optional;

// Service class for managing Digital Twin State Event Notification records
public class PostgresDigitalTwinStateEventNotificationService {
    // Default constructor
    public PostgresDigitalTwinStateEventNotificationService() {
    }

    /* Saves the Digital Twin State Event Notification record
    @param record  The WLDT core record to save
     */
    public void saveRecord(DigitalTwinStateEventNotificationRecord record) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
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
