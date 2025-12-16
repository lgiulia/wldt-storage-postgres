package it.wldt.storage.postgres.service.lifecycle;

import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.lifecycle.LifeCycleVariationRecord;

import java.util.List;
import java.util.Optional;

// Service class for managing Life Cycle State records in PostgreSQL
public class PostgresLifeCycleStateService {

    // Constructor
    public PostgresLifeCycleStateService() {
    }

    /* Saves a LifeCycleVariationRecord to the database
    @param lifeCycleVariation  the record to save
     */
    public void saveRecord(LifeCycleVariationRecord lifeCycleVariationRecord) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
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