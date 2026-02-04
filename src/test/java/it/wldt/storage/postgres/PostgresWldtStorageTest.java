package it.wldt.storage.postgres;

import it.wldt.adapter.digital.DigitalActionRequest;
import it.wldt.adapter.physical.*;
import it.wldt.core.engine.LifeCycleState;
import it.wldt.core.engine.LifeCycleStateVariation;
import it.wldt.core.state.*;
import it.wldt.exception.StorageException;
import it.wldt.exception.WldtDigitalTwinStateException;
import it.wldt.exception.WldtDigitalTwinStatePropertyException;
import it.wldt.storage.model.digital.DigitalActionRequestRecord;
import it.wldt.storage.model.lifecycle.LifeCycleVariationRecord;
import it.wldt.storage.model.physical.PhysicalAssetActionRequestRecord;
import it.wldt.storage.model.physical.PhysicalAssetDescriptionNotificationRecord;
import it.wldt.storage.model.physical.PhysicalAssetEventNotificationRecord;
import it.wldt.storage.model.physical.PhysicalAssetPropertyVariationRecord;
import it.wldt.storage.model.state.DigitalTwinStateEventNotificationRecord;
import it.wldt.storage.model.state.DigitalTwinStateRecord;
import it.wldt.storage.postgres.model.common.PostgresWldtStorageConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PostgresWldtStorageTest {
    private static final Logger logger = LoggerFactory.getLogger(PostgresWldtStorageTest.class);

    private final String DIGITAL_TWIN_ID = "dt-test-01";
    private final String STORAGE_ID = "postgres-test";

    private PostgresWldtStorage storage;
    private DigitalTwinStateManager digitalTwinStateManager;

    @BeforeEach
    public void setUp() throws StorageException, WldtDigitalTwinStateException {
        logger.info("----Setting Up Test----");

        digitalTwinStateManager = new DigitalTwinStateManager(DIGITAL_TWIN_ID);

        PostgresWldtStorageConfiguration config = new PostgresWldtStorageConfiguration("test-config-id");

        storage = new PostgresWldtStorage(STORAGE_ID, true, config);
    }

    @Test
    public void testInitialization() {
        assertNotNull(storage, "Storage Inizialized");
        logger.info("Test Initialization: PASSED");
    }

    // Test to save Digital Twin State
    @Test
    public void testSaveDigitalTwinState() throws WldtDigitalTwinStateException, StorageException {
        logger.info("Testing saveDigitalTwinState...");

        DigitalTwinStateProperty<String> property = new DigitalTwinStateProperty<>("temperature", "25.0");
        digitalTwinStateManager.startStateTransaction();
        digitalTwinStateManager.createProperty(property);
        digitalTwinStateManager.commitStateTransaction();

        DigitalTwinState state = digitalTwinStateManager.getDigitalTwinState();
        List<DigitalTwinStateChange> changes = new ArrayList<>();

        // Data saving
        storage.saveDigitalTwinState(state, changes);

        // Data reading
        Optional<DigitalTwinStateRecord> lastStateOpt = storage.getLastDigitalTwinState();
        assertTrue(lastStateOpt.isPresent(), "Error: no record found!");
        if (lastStateOpt.isPresent()) {
            DigitalTwinStateRecord record = lastStateOpt.get();
            assertNotNull(record.getCurrentState(), "State can't be null");
            String stateAsString = record.getCurrentState().toString();
            logger.info("       -> State: " + stateAsString);
            if (stateAsString.contains("temperature")) {
                logger.info("       -> String contains 'temperature'");
            }
        }

        int count = storage.getDigitalTwinStateCount();
        logger.info("Total Count: " + count);
        assertTrue(count > 0);

        logger.info("Test saveDigitalTwinState: PASSED");
    }

    // Test to save Digital Twin State Event Notification
    @Test
    public void testDigitalTwinStateEventNotification() throws StorageException {
        logger.info("Testing digitalTwinStateEventNotification...");

        long timestamp = System.currentTimeMillis();
        String eventKey = "computation-completed";
        String eventBody = "Result: 99,9%";

        DigitalTwinStateEventNotification<String> notification = new DigitalTwinStateEventNotification<>(
                eventKey,
                eventBody,
                System.currentTimeMillis()
        );

        // Data saving
        storage.saveDigitalTwinStateEventNotification(notification);

        // Data reading
        int count = storage.getDigitalTwinStateEventNotificationCount();
        logger.info("Records found: " + count);
        assertNotNull(count);

        long start = timestamp - 5000;
        long end = timestamp + 5000;
        List<DigitalTwinStateEventNotificationRecord> timeList = storage.getDigitalTwinStateEventNotificationInTimeRange(start, end);
        logger.info("Time Range (" + start + " - " + end + ") ---> " + timeList.size());
        if (!timeList.isEmpty()) {
            DigitalTwinStateEventNotificationRecord r = timeList.get(0);
            logger.info("       -> Key='" + r.getEventKey() + "' Timestamp=" + r.getTimestamp());
            assertEquals(eventKey, r.getEventKey());
            assertEquals(eventBody, r.getBody());
        }

        if (count > 0) {
            int startIndex = 0;
            int endIndex = 1;
            List<DigitalTwinStateEventNotificationRecord> pageList = storage.getDigitalTwinStateEventNotificationInRange(startIndex, endIndex);
            logger.info("Pagination range (Index " + startIndex + " - " + endIndex + ") ---> " + pageList.size());
        }

        logger.info("Test saveDigitalTwinStateEventNotification: PASSED");
    }

    // Test to save Digital Twin Lifecycle
    @Test
    public void testSaveLifeCycleState() throws StorageException{
        logger.info("Testing saveLifeCycleState...");

        long timestamp  = System.currentTimeMillis();
        LifeCycleState expectedState = LifeCycleState.STARTED;

        LifeCycleStateVariation variation = new LifeCycleStateVariation(
                timestamp,
                expectedState
        );

        // Data saving
        storage.saveLifeCycleState(variation);

        // Data reading
        LifeCycleVariationRecord lastRecord = storage.getLastLifeCycleState();
        assertNotNull(lastRecord, "Last record can't be null");
        logger.info("Last Record State: " + lastRecord.getLifeCycleState());
        assertEquals(expectedState, lastRecord.getLifeCycleState(), "Read Lifecycle state equals written Lifecycle state");

        int count = storage.getLifeCycleStateCount();
        logger.info("Total Count: " + count);
        assertTrue(count > 0);

        long start = timestamp - 5000;
        long end = timestamp + 5000;
        List<LifeCycleVariationRecord> timeList = storage.getLifeCycleStateInTimeRange(start, end);
        logger.info("Time Range: Found " + timeList.size());
        assertFalse(timeList.isEmpty());
        assertEquals(expectedState, timeList.get(0).getLifeCycleState());

        logger.info("Test saveLifeCycleState: PASSED");
    }

    // Test to save Physical Asset Event Notification
    @Test
    public void testPhysicalEventNotification() throws StorageException {
        logger.info("Testing PhysicalAssetEventNotification...");

        long timestamp = System.currentTimeMillis();

        // Data creation
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("sensor-id", "temp-001");

        PhysicalAssetEventNotification notification = new PhysicalAssetEventNotification(
                timestamp,
                "alarm-high-temp",
                "Temperature is over 100!",
                metadata
        );

        // Data saving
        storage.savePhysicalAssetEventNotification(notification);

        // Data reading
        int count = storage.getPhysicalAssetEventNotificationCount();
        logger.info("Records found: " + count);
        assertNotNull(count);

        long start = timestamp - 5000;
        long end = timestamp + 5000;
        List<PhysicalAssetEventNotificationRecord> timeList = storage.getPhysicalAssetEventNotificationInTimeRange(start, end);
        logger.info("Time Range (" + start + " - " + end + ") ---> " + timeList.size());
        if (!timeList.isEmpty()) {
            PhysicalAssetEventNotificationRecord r = timeList.get(0);
            logger.info("       -> Key='" + r.getEventkey() + "' Timestamp=" + r.getTimestamp());
            assertEquals("temp-001", r.getMetadata().get("sensor-id"));
        }

        if (count > 0) {
            int startIndex = 0;
            int endIndex = 1;
            List<PhysicalAssetEventNotificationRecord> pageList = storage.getPhysicalAssetEventNotificationInRange(startIndex, endIndex);
            logger.info("Pagination range (Index " + startIndex + " - " + endIndex + ") ---> " + pageList.size());
        }

        logger.info("Test PhysicalAssetEventNotification: PASSED");
    }

    // Test for PhysicalAssetActionRequest
    @Test
    public void testPhysicalAssetActionRequest() throws StorageException {
        logger.info("Testing PhysicalAssetActionRequest...");

        long timestamp = System.currentTimeMillis();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("user-initiator", "admin");

        String uniqueKey = "switch-off-light";
        String jsonBody = "{\"zone\":\"kitchen\" }";

        PhysicalAssetActionRequest request = new PhysicalAssetActionRequest(
                System.currentTimeMillis(),
                uniqueKey,
                jsonBody,
                metadata
        );

        // Data saving
        storage.savePhysicalAssetActionRequest(request);

        // Data reading
        int count = storage.getPhysicalAssetActionRequestCount();
        logger.info("Records found: " + count);
        assertNotNull(count);

        long start = timestamp - 5000;
        long end = timestamp + 5000;
        List<PhysicalAssetActionRequestRecord> timeList = storage.getPhysicalAssetActionRequestInTimeRange(start, end);
        logger.info("Time Range (" + start + " - " + end + ") ---> " + timeList.size());
        if (!timeList.isEmpty()) {
            PhysicalAssetActionRequestRecord r = timeList.get(0);
            logger.info("       -> Key='" + r.getActionkey() + "' Timestamp=" + r.getRequestTimestamp());
            assertEquals(uniqueKey, r.getActionkey());
            assertEquals("admin", r.getRequestMetadata().get("user-initiator"));
        }

        if (count > 0) {
            int startIndex = 0;
            int endIndex = 1;
            List<PhysicalAssetActionRequestRecord> pageList = storage.getPhysicalAssetActionRequestInRange(startIndex, endIndex);
            logger.info("Pagination range (Index " + startIndex + " - " + endIndex + ") ---> " + pageList.size());
        }

        logger.info("Test Physical Asset Action Request: PASSED");
    }

    // Test for PhysicalAssetPropertyVariation
    @Test
    public void testPhysicalAssetPropertyVariation() throws StorageException {
        logger.info("Testing PhysicalAssetPropertyVariation...");

        long timestamp = System.currentTimeMillis();
        String propertyKey = "temperature";
        double propertyValue = 26.5;

        PhysicalAssetPropertyVariation variation = new PhysicalAssetPropertyVariation(
                timestamp,
                propertyKey,
                propertyValue,
                new HashMap<>()
        );

        // Data saving
        storage.savePhysicalAssetPropertyVariation(variation);

        // Data reading
        int count = storage.getPhysicalAssetPropertyVariationCount();
        logger.info("Records found: " + count);
        assertNotNull(count);

        long start = timestamp - 5000;
        long end = timestamp + 5000;
        List<PhysicalAssetPropertyVariationRecord> timeList = storage.getPhysicalAssetPropertyVariationInTimeRange(start, end);
        logger.info("Time Range (" + start + " - " + end + ") ---> " + timeList.size());
        if (!timeList.isEmpty()) {
            PhysicalAssetPropertyVariationRecord r = timeList.get(0);
            logger.info("       -> Key='" + r.getPropertykey() + "' Timestamp=" + r.getTimestamp());
            assertEquals(propertyKey, r.getPropertykey());
            assertEquals(propertyValue, r.getBody());
        }

        if (count > 0) {
            int startIndex = 0;
            int endIndex = 1;
            List<PhysicalAssetPropertyVariationRecord> pageList = storage.getPhysicalAssetPropertyVariationInRange(startIndex, endIndex);
            logger.info("Pagination range (Index " + startIndex + " - " + endIndex + ") ---> " + pageList.size());
        }

        logger.info("Test PhysicalAssetPropertyVariation: PASSED");
    }

    // Test for PhysicalAssetDescriptionNotification
    @Test
    public void testPhysicalAssetDescriptionNotification() throws StorageException {
        logger.info("Testing PhysicalAssetDescriptionNotification...");

        long timestamp = System.currentTimeMillis();
        String adapterId = "test-adapter-01";

        List<PhysicalAssetProperty<?>> properties = new ArrayList<>();
        properties.add(new PhysicalAssetProperty<>("temperature", 20.0));
        properties.add(new PhysicalAssetProperty<>("status", "active"));

        List<PhysicalAssetAction> actions = new ArrayList<>();
        actions.add(new PhysicalAssetAction("switch-on", "action.switch", "application/json"));

        List<PhysicalAssetEvent> events = new ArrayList<>();
        events.add(new PhysicalAssetEvent("overheating-alarm", "event.alarm"));

        PhysicalAssetDescription description = new PhysicalAssetDescription(actions, properties, events);

        PhysicalAssetDescriptionNotification notification = new PhysicalAssetDescriptionNotification(
                System.currentTimeMillis(),
                adapterId,
                description
        );

        // Data saving
        storage.saveNewPhysicalAssetDescriptionNotification(notification);

        // Data reading
        int count = storage.getNewPhysicalAssetDescriptionNotificationCount();
        logger.info("Records found: " + count);
        assertNotNull(count);

        long start = timestamp - 5000;
        long end = timestamp + 5000;
        List<PhysicalAssetDescriptionNotificationRecord> timeList = storage.getNewPhysicalAssetDescriptionNotificationInTimeRange(start, end);
        logger.info("Time Range (" + start + " - " + end + ") ---> " + timeList.size());
        if (!timeList.isEmpty()) {
            PhysicalAssetDescriptionNotificationRecord r = timeList.get(0);
            logger.info("       -> Key='" + r.getAdapterId() + "' Timestamp=" + r.getNotificationTimestamp());
            assertEquals(adapterId, r.getAdapterId());
            assertNotNull(r.getPhysicalAssetDescription(), "Description can't be null");
            assertFalse(r.getPhysicalAssetDescription().getProperties().isEmpty(), "Properties can't be empty");
            logger.info("       -> Properties found: " + r.getPhysicalAssetDescription().getProperties().size());
        }

        if (count > 0) {
            int startIndex = 0;
            int endIndex = 1;
            List<PhysicalAssetDescriptionNotificationRecord> pageList = storage.getNewPhysicalAssetDescriptionNotificationInRange(startIndex, endIndex);
            logger.info("Pagination range (Index " + startIndex + " - " + endIndex + ") ---> " + pageList.size());
        }

        logger.info("Test PhysicalAssetDescriptionNotification: PASSED");
    }

    // Test to save Updated Physical Asset Description Notification
    @Test
    public void testUpdatedPhysicalAssetDescriptionNotification() throws StorageException {
        logger.info("Testing updatedPhysicalAssetDescriptionNotification...");

        List<PhysicalAssetProperty<?>> properties = new ArrayList<>();
        properties.add(new PhysicalAssetProperty<>("temperature", 22.0));

        List<PhysicalAssetAction> actions = new ArrayList<>();
        actions.add(new PhysicalAssetAction("switch-off", "action.switch", "application/json"));

        List<PhysicalAssetEvent> events = new ArrayList<>();

        PhysicalAssetDescription description = new PhysicalAssetDescription(actions, properties, events);

        PhysicalAssetDescriptionNotification notification = new PhysicalAssetDescriptionNotification(
                System.currentTimeMillis(),
                "test-adapter-01-update",
                description
        );

        storage.saveUpdatedPhysicalAssetDescriptionNotification(notification);

        logger.info("Test updatedPhysicalAssetDescriptionNotification: PASSED");
    }

    // Test to save Physical Relationship Instance Variation
    @Test
    public void testPhysicalRelationshipInstanceVariation() throws StorageException {
        logger.info("Testing PhysicalRelationshipInstanceVariation...");

        PhysicalAssetRelationship<String> relationship = new PhysicalAssetRelationship<>("connected-to", "parent-child");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("signal-strength", "strong");

        PhysicalAssetRelationshipInstance<String> instance = new PhysicalAssetRelationshipInstance<>(
                relationship,
                "charging-station-x",
                metadata
        );

        // Relationship creation saved
        PhysicalRelationshipInstanceVariation createVariation = new PhysicalRelationshipInstanceVariation(
                System.currentTimeMillis(),
                instance
        );
        storage.savePhysicalAssetRelationshipInstanceCreatedNotification(createVariation);

        // Relationship cancellation saved
        PhysicalRelationshipInstanceVariation deleteVariation = new PhysicalRelationshipInstanceVariation(
                System.currentTimeMillis() + 1000,
                instance
        );
        storage.savePhysicalAssetRelationshipInstanceDeletedNotification(deleteVariation);

        logger.info("Test PhysicalRelationshipInstanceVariation: PASSED");
    }

    // Test to save Digital Action Request Service
    @Test
    public void testDigitalActionRequest() throws StorageException {
        logger.info("Testing DigitalActionRequest...");

        long timestamp = System.currentTimeMillis();

        // Data creation
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("user-role", "admin");
        metadata.put("priority", "high");

        String uniqueKey = "backup-system-" + timestamp;

        DigitalActionRequest request = new DigitalActionRequest(
                timestamp,
                uniqueKey,
                "full-backup",
                metadata
        );

        // Data saving
        storage.saveDigitalActionRequest(request);

        // Data reading
        int count = storage.getDigitalActionRequestCount();
        logger.info("Records found: " + count);
        assertNotNull(count);

        long start = timestamp - 5000;
        long end = timestamp + 5000;
        List<DigitalActionRequestRecord> timeList = storage.getDigitalActionRequestInTimeRange(start, end);
        logger.info("Time Range (" + start + " - " + end + ") ---> " + timeList.size());
        if (!timeList.isEmpty()) {
            DigitalActionRequestRecord r = timeList.get(0);
            logger.info("       -> Key='" + r.getActionkey() + "' Timestamp=" + r.getRequestTimestamp());
            assertEquals("admin", r.getRequestMetadata().get("user-role"));
        }

        if (count > 0) {
            int startIndex = 0;
            int endIndex = 1;
            List<DigitalActionRequestRecord> pageList = storage.getDigitalActionRequestInRange(startIndex, endIndex);
            logger.info("Pagination range (Index " + startIndex + " - " + endIndex + ") ---> " + pageList.size());
        }

        logger.info("Test DigitalActionRequest: PASSED");
    }
}
