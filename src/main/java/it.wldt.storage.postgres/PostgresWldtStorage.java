package it.wldt.storage.postgres;

import it.wldt.adapter.digital.DigitalActionRequest;
import it.wldt.adapter.physical.*;
import it.wldt.core.engine.LifeCycleStateVariation;
import it.wldt.core.state.DigitalTwinState;
import it.wldt.core.state.DigitalTwinStateChange;
import it.wldt.core.state.DigitalTwinStateEventNotification;
import it.wldt.exception.StorageException;
import it.wldt.storage.WldtStorage;

import it.wldt.storage.model.StorageStats;
import it.wldt.storage.model.digital.DigitalActionRequestRecord;
import it.wldt.storage.model.lifecycle.LifeCycleVariationRecord;
import it.wldt.storage.model.physical.*;
import it.wldt.storage.model.state.DigitalTwinStateEventNotificationRecord;
import it.wldt.storage.model.state.DigitalTwinStateRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtStorageConfiguration;
import it.wldt.storage.postgres.service.digital.PostgresDigitalActionRequestService;
import it.wldt.storage.postgres.service.lifecycle.PostgresLifeCycleStateService;
import it.wldt.storage.postgres.service.physical.*;
import it.wldt.storage.postgres.service.state.PostgresDigitalTwinStateEventNotificationService;
import it.wldt.storage.postgres.service.state.PostgresDigitalTwinStateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PostgresWldtStorage extends WldtStorage {

    private static final Logger logger = LoggerFactory.getLogger(PostgresWldtStorage.class);
    private final PostgresWldtStorageConfiguration configuration;

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "postgres";

    private Connection connection;

    private PostgresDigitalTwinStateService digitalTwinStateService;
    private PostgresDigitalTwinStateEventNotificationService digitalTwinStateEventNotificationService;
    private PostgresLifeCycleStateService lifeCycleStateService;
    private PostgresPhysicalAssetEventNotificationService physicalAssetEventNotificationService;
    private PostgresPhysicalAssetActionRequestService physicalAssetActionRequestService;
    private PostgresDigitalActionRequestService digitalActionRequestService;
    private PostgresPhysicalAssetDescriptionNotificationService physicalAssetDescriptionNotificationService;
    private PostgresPhysicalAssetDescriptionNotificationService updatedPhysicalAssetDescriptionNotificationService;
    private PostgresPhysicalAssetPropertyVariationService physicalAssetPropertyVariationService;
    private PostgresPhysicalRelationshipInstanceVariationService physicalRelationshipInstanceVariationService;

    public PostgresWldtStorage(String storageId, boolean observeAll, PostgresWldtStorageConfiguration configuration) throws StorageException {
        super(storageId, observeAll);
        this.configuration = configuration;
        try {
            this.init();
        } catch (Exception e) {
            throw new StorageException("Error while initializing PostgresWldtStorage: " + e.getMessage());
        }
    }

    public PostgresWldtStorage(String storageId,
                               boolean observeStateEvents,
                               boolean observerPhysicalAssetEvents,
                               boolean observerPhysicalAssetActionEvents,
                               boolean observePhysicalAssetDescriptionEvents,
                               boolean observerDigitalActionEvents,
                               boolean observeLifeCycleEvents,
                               PostgresWldtStorageConfiguration configuration) throws StorageException {
        super(storageId, observeStateEvents, observerPhysicalAssetEvents, observerPhysicalAssetActionEvents, observePhysicalAssetDescriptionEvents, observerDigitalActionEvents, observeLifeCycleEvents);
        this.configuration = configuration;
        try {
            this.init();
        } catch (Exception e) {
            throw new StorageException("Error while initializing PostgresWldtStorage: " + e.getMessage());
        }
    }

    // Service/Connection methods
    @Override
    protected void init() {
        logger.info("Initializing PostgresWldtStorage...");
        try {
            // Upload driver
            Class.forName("org.postgresql.Driver");
            // Open the connection
            this.connection = DriverManager.getConnection(url, user, password);
            logger.info("Connected to PostgreSQL database successfully!");
            // Services initialize
            this.initServices();
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL JDBC Driver not found!", e);
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        } catch (SQLException e) {
            logger.error("Connection to PostgreSQL failed!", e);
            throw new RuntimeException("Connection to PostgreSQL failed", e);
        }
    }

    private void initServices() {
        this.digitalTwinStateService = new PostgresDigitalTwinStateService(this.connection);
        this.digitalTwinStateEventNotificationService = new PostgresDigitalTwinStateEventNotificationService(this.connection);
        this.lifeCycleStateService = null;
        this.physicalAssetEventNotificationService = new PostgresPhysicalAssetEventNotificationService(this.connection);
        this.physicalAssetActionRequestService = new PostgresPhysicalAssetActionRequestService(this.connection);
        this.digitalActionRequestService = new PostgresDigitalActionRequestService(this.connection);
        this.physicalAssetDescriptionNotificationService = new PostgresPhysicalAssetDescriptionNotificationService(this.connection);
        this.updatedPhysicalAssetDescriptionNotificationService = null;
        this.physicalAssetPropertyVariationService = new PostgresPhysicalAssetPropertyVariationService(this.connection);
        this.physicalRelationshipInstanceVariationService = new PostgresPhysicalRelationshipInstanceVariationService(this.connection);
    }

    private void checkRange(int startIndex, int endIndex, int valuesCount) {
        if (startIndex < 0 || endIndex < 0 || startIndex > endIndex) {
            throw new IllegalArgumentException("Invalid index range");
        }
        if (endIndex >= valuesCount) {
            throw new IndexOutOfBoundsException("End index out of bounds");
        }
    }

    private void checkTimeRange(long startTimeStampMs, long endTimeStampMs) {
        if (startTimeStampMs > endTimeStampMs) {
            throw new IllegalArgumentException("Start timestamp cannot be greater than end timestamp");
        }
    }

    @Override
    public void saveDigitalTwinState(DigitalTwinState digitalTwinState, List<DigitalTwinStateChange> digitalTwinStateChangeList) throws StorageException, IllegalArgumentException {
        if (this.digitalTwinStateService != null) {
            // Creation of the record
            DigitalTwinStateRecord record = new DigitalTwinStateRecord(digitalTwinState, digitalTwinStateChangeList);
            // Saving of the record
            this.digitalTwinStateService.saveRecord(record);
        } else {
            throw new StorageException("DigitalTwinStateService is null");
        }
    }

    @Override
    public Optional<DigitalTwinStateRecord> getLastDigitalTwinState() throws StorageException {
        return Optional.empty();
    }

    @Override
    public int getDigitalTwinStateCount() throws StorageException {
        return 0;
    }

    @Override
    public List<DigitalTwinStateRecord> getDigitalTwinStateInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<DigitalTwinStateRecord> getDigitalTwinStateInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void saveDigitalTwinStateEventNotification(DigitalTwinStateEventNotification<?> digitalTwinStateEventNotification) throws StorageException {
        if (this.digitalTwinStateEventNotificationService != null) {
            DigitalTwinStateEventNotificationRecord record = new DigitalTwinStateEventNotificationRecord(
                    digitalTwinStateEventNotification.getDigitalEventKey(),
                    digitalTwinStateEventNotification.getBody(),
                    digitalTwinStateEventNotification.getTimestamp()
            );
            this.digitalTwinStateEventNotificationService.saveRecord(record);
        }
    }

    @Override
    public int getDigitalTwinStateEventNotificationCount() throws StorageException {
        return 0;
    }

    @Override
    public List<DigitalTwinStateEventNotificationRecord> getDigitalTwinStateEventNotificationInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<DigitalTwinStateEventNotificationRecord> getDigitalTwinStateEventNotificationInRange(int startIndex, int endIndex) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void saveLifeCycleState(LifeCycleStateVariation lifeCycleStateVariation) throws StorageException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public LifeCycleVariationRecord getLastLifeCycleState() throws StorageException {
        return null;
    }

    @Override
    public int getLifeCycleStateCount() throws StorageException {
        return 0;
    }

    @Override
    public List<LifeCycleVariationRecord> getLifeCycleStateInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<LifeCycleVariationRecord> getLifeCycleStateInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void savePhysicalAssetEventNotification(PhysicalAssetEventNotification physicalAssetEventNotification) throws StorageException {
        if (this.physicalAssetEventNotificationService != null) {
            PhysicalAssetEventNotificationRecord record = new PhysicalAssetEventNotificationRecord(
                    physicalAssetEventNotification.getTimestamp(),
                    physicalAssetEventNotification.getEventkey(),
                    physicalAssetEventNotification.getBody(),
                    physicalAssetEventNotification.getMetadata()
            );
            this.physicalAssetEventNotificationService.saveRecord(record);
        }
    }

    @Override
    public int getPhysicalAssetEventNotificationCount() throws StorageException {
        return 0;
    }

    @Override
    public List<PhysicalAssetEventNotificationRecord> getPhysicalAssetEventNotificationInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<PhysicalAssetEventNotificationRecord> getPhysicalAssetEventNotificationInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void savePhysicalAssetActionRequest(PhysicalAssetActionRequest physicalAssetActionRequest) throws StorageException {
        if (this.physicalAssetActionRequestService != null) {
            PhysicalAssetActionRequestRecord record = new PhysicalAssetActionRequestRecord(
                    physicalAssetActionRequest.getRequestTimestamp(),
                    physicalAssetActionRequest.getActionkey(),
                    physicalAssetActionRequest.getRequestBody(),
                    physicalAssetActionRequest.getRequestMetadata()
            );
            this.physicalAssetActionRequestService.saveRecord(record);
        }
    }

    @Override
    public int getPhysicalAssetActionRequestCount() throws StorageException {
        return 0;
    }

    @Override
    public List<PhysicalAssetActionRequestRecord> getPhysicalAssetActionRequestInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<PhysicalAssetActionRequestRecord> getPhysicalAssetActionRequestInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void saveDigitalActionRequest(DigitalActionRequest digitalActionRequest) throws StorageException {
        if (this.digitalActionRequestService != null) {
            DigitalActionRequestRecord record = new DigitalActionRequestRecord(
                    digitalActionRequest.getRequestTimestamp(),
                    digitalActionRequest.getActionkey(),
                    digitalActionRequest.getRequestBody(),
                    digitalActionRequest.getRequestMetadata()
            );
            this.digitalActionRequestService.saveRecord(record);
        }
    }

    @Override
    public int getDigitalActionRequestCount() throws StorageException {
        return 0;
    }

    @Override
    public List<DigitalActionRequestRecord> getDigitalActionRequestInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<DigitalActionRequestRecord> getDigitalActionRequestInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void saveNewPhysicalAssetDescriptionNotification(PhysicalAssetDescriptionNotification physicalAssetDescriptionNotification) throws StorageException {
        if (this.physicalAssetDescriptionNotificationService != null) {
            PhysicalAssetDescriptionNotificationRecord record = new PhysicalAssetDescriptionNotificationRecord(
                    physicalAssetDescriptionNotification.getNotificationTimestamp(),
                    physicalAssetDescriptionNotification.getAdapterId(),
                    physicalAssetDescriptionNotification.getPhysicalAssetDescription()
            );
            this.physicalAssetDescriptionNotificationService.saveRecord(record);
        }
    }

    @Override
    public int getNewPhysicalAssetDescriptionNotificationCount() throws StorageException {
        return 0;
    }

    @Override
    public List<PhysicalAssetDescriptionNotificationRecord> getNewPhysicalAssetDescriptionNotificationInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<PhysicalAssetDescriptionNotificationRecord> getNewPhysicalAssetDescriptionNotificationInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void saveUpdatedPhysicalAssetDescriptionNotification(PhysicalAssetDescriptionNotification physicalAssetDescriptionNotification) throws StorageException {

    }

    @Override
    public int getUpdatedPhysicalAssetDescriptionNotificationCount() throws StorageException {
        return 0;
    }

    @Override
    public List<PhysicalAssetDescriptionNotificationRecord> getUpdatedPhysicalAssetDescriptionNotificationInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<PhysicalAssetDescriptionNotificationRecord> getUpdatedPhysicalAssetDescriptionNotificationInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void savePhysicalAssetPropertyVariation(PhysicalAssetPropertyVariation physicalAssetPropertyVariation) throws StorageException {
        if (this.physicalAssetPropertyVariationService != null) {
            PhysicalAssetPropertyVariationRecord record = new PhysicalAssetPropertyVariationRecord(
                    physicalAssetPropertyVariation.getTimestamp(),
                    physicalAssetPropertyVariation.getPropertykey(),
                    physicalAssetPropertyVariation.getBody(),
                    physicalAssetPropertyVariation.getVariationMetadata()
            );
            this.physicalAssetPropertyVariationService.saveRecord(record);
        }
    }

    @Override
    public int getPhysicalAssetPropertyVariationCount() throws StorageException {
        return 0;
    }

    @Override
    public List<PhysicalAssetPropertyVariationRecord> getPhysicalAssetPropertyVariationInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<PhysicalAssetPropertyVariationRecord> getPhysicalAssetPropertyVariationInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void savePhysicalAssetRelationshipInstanceCreatedNotification(PhysicalRelationshipInstanceVariation physicalRelationshipInstanceVariation) throws StorageException {
        if (this.physicalRelationshipInstanceVariationService != null) {

            PhysicalAssetRelationshipInstance<?> instance = physicalRelationshipInstanceVariation.getPhysicalAssetRelationshipInstance();

            PhysicalRelationshipInstanceVariationRecord record = new PhysicalRelationshipInstanceVariationRecord(
                    physicalRelationshipInstanceVariation.getNotificationTimestamp(),
                    instance.getKey(),
                    instance.getTargetId(),
                    instance.getRelationship().getName(),
                    "created",
                    instance.getMetadata().orElse(new HashMap<>())
            );

            this.physicalRelationshipInstanceVariationService.saveRecord(record);
        }
    }

    @Override
    public void savePhysicalAssetRelationshipInstanceDeletedNotification(PhysicalRelationshipInstanceVariation physicalRelationshipInstanceVariation) throws StorageException {
        if (this.physicalRelationshipInstanceVariationService != null) {

            PhysicalAssetRelationshipInstance<?> instance = physicalRelationshipInstanceVariation.getPhysicalAssetRelationshipInstance();

            PhysicalRelationshipInstanceVariationRecord record = new PhysicalRelationshipInstanceVariationRecord(
                    physicalRelationshipInstanceVariation.getNotificationTimestamp(),
                    instance.getKey(),
                    instance.getTargetId(),
                    instance.getRelationship().getName(),
                    "deleted",
                    instance.getMetadata().orElse(new HashMap<>())
            );

            this.physicalRelationshipInstanceVariationService.saveRecord(record);
        }
    }

    @Override
    public int getPhysicalAssetRelationshipInstanceCreatedNotificationCount() throws StorageException {
        return 0;
    }

    @Override
    public List<PhysicalRelationshipInstanceVariationRecord> getPhysicalAssetRelationshipInstanceCreatedNotificationInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<PhysicalRelationshipInstanceVariationRecord> getPhysicalAssetRelationshipInstanceCreatedNotificationInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public int getPhysicalAssetRelationshipInstanceDeletedNotificationCount() throws StorageException {
        return 0;
    }

    @Override
    public List<PhysicalRelationshipInstanceVariationRecord> getPhysicalAssetRelationshipInstanceDeletedNotificationInTimeRange(long startTimestampMs, long endTimestampMs) throws StorageException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public List<PhysicalRelationshipInstanceVariationRecord> getPhysicalAssetRelationshipInstanceDeletedNotificationInRange(int startIndex, int endIndex) throws StorageException, IndexOutOfBoundsException, IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public StorageStats getStorageStats() throws StorageException {
        return null;
    }

    @Override
    protected void clear() throws StorageException {

    }
}