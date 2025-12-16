package it.wldt.storage.postgres.service.digital;

import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.digital.DigitalActionRequestRecord;

import java.util.List;
import java.util.Optional;

// Service class for managing Digital Action Request records in PostgreSQL
public class PostgresDigitalActionRequestService {

    // Constructor
    public PostgresDigitalActionRequestService() {
    }

    /* Saves a DigitalActionRequestRecord in the database
    @param digitalActionRequest  The DigitalActionRequestRecord to save
     */
    public void saveRecord(DigitalActionRequestRecord digitalActionRequest) throws StorageException {
    }

    // Methods to access data
    public Optional<DigitalActionRequestRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<DigitalActionRequestRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<DigitalActionRequestRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}