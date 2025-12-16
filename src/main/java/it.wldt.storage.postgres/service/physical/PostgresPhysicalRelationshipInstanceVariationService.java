package it.wldt.storage.postgres.service.physical;

import it.wldt.exception.StorageException;
import it.wldt.storage.model.StorageStatsRecord;
import it.wldt.storage.model.physical.PhysicalRelationshipInstanceVariationRecord;

import java.util.List;
import java.util.Optional;

// Service class for managing Physical Relationship Instance Variation records
public class PostgresPhysicalRelationshipInstanceVariationService {
    // Default constructor
    public PostgresPhysicalRelationshipInstanceVariationService() {}

    /* Saves a PhysicalRelationshipInstanceVariationRecord
    @param record  The WLDT core record to save
     */
    public void saveRecord(PhysicalRelationshipInstanceVariationRecord record) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    // Methods to access data
    public Optional<PhysicalRelationshipInstanceVariationRecord> getLastRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getRecordsCount() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalRelationshipInstanceVariationRecord> getRecordsInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<PhysicalRelationshipInstanceVariationRecord> getRecordsInRange(int startIndex, int endIndex) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public StorageStatsRecord getStorageStatsRecord() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteAllRecords() throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
