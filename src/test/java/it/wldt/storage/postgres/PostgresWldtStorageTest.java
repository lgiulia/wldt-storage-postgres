package it.wldt.storage.postgres;

import it.wldt.adapter.digital.DigitalActionRequest;
import it.wldt.adapter.physical.*;
import it.wldt.core.engine.LifeCycleState;
import it.wldt.core.engine.LifeCycleStateVariation;
import it.wldt.core.state.*;
import it.wldt.exception.StorageException;
import it.wldt.exception.WldtDigitalTwinStateException;
import it.wldt.storage.postgres.model.common.PostgresWldtStorageConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        storage.saveDigitalTwinState(state, changes);

        logger.info("Test saveDigitalTwin: PASSED");
    }

    // Test to save Digital Twin State Event Notification
    @Test
    public void testDigitalTwinStateEventNotification() throws StorageException {
        logger.info("Testing digitalTwinStateEventNotification...");

        DigitalTwinStateEventNotification<String> notification = new DigitalTwinStateEventNotification<>(
                "computation-completed",
                "Result: 99,9%",
                System.currentTimeMillis()
        );

        storage.saveDigitalTwinStateEventNotification(notification);

        logger.info("Test saveDigitalTwinStateEventNotification: PASSED");
    }

    // Test to save Digital Twin Lifecycle
    @Test
    public void testSaveLifeCycleState() throws StorageException{
        logger.info("Testing saveLifeCycleState...");

        LifeCycleStateVariation variation = new LifeCycleStateVariation(
                System.currentTimeMillis(),
                LifeCycleState.STARTED
        );

        storage.saveLifeCycleState(variation);

        logger.info("Test saveLifeCycleState: PASSED");
    }

    // Test to save Physical Asset Event Notification
    @Test
    public void testPhysicalEventNotification() throws StorageException {
        logger.info("Testing PhysicalAssetEventNotification...");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("sensor-id", "temp-001");

        PhysicalAssetEventNotification notification = new PhysicalAssetEventNotification(
                System.currentTimeMillis(),
                "alarm-high-temp",
                "Temperature is over 100!",
                metadata
        );

        storage.savePhysicalAssetEventNotification(notification);

        logger.info("Test PhysicalEventNotification: PASSED");
    }

    // Test for PhysicalAssetActionRequest
    @Test
    public void testPhysicalAssetActionRequest() throws StorageException {
        logger.info("Testing PhysicalAssetActionRequest...");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("user-initiator", "admin");

        PhysicalAssetActionRequest request = new PhysicalAssetActionRequest(
                System.currentTimeMillis(),
                "switch-off-light",
                "{\"zone\":\"kitchen\" }",
                metadata
        );

        storage.savePhysicalAssetActionRequest(request);

        logger.info("Test Action Request: PASSED");
    }

    // Test for PhysicalAssetPropertyVariation
    @Test
    public void testPhysicalAssetPropertyVariation() throws StorageException {
        logger.info("Testing PhysicalAssetPropertyVariation...");

        PhysicalAssetPropertyVariation variation = new PhysicalAssetPropertyVariation(
                System.currentTimeMillis(),
                "temperature",
                26.5,
                new HashMap<>()
        );

        storage.savePhysicalAssetPropertyVariation(variation);

        logger.info("Test PropertyVariation: PASSED");
    }

    // Test for PhysicalAssetDescriptionNotification
    @Test
    public void testPhysicalAssetDescriptionNotification() throws StorageException {
        logger.info("Testing PhysicalAssetDescriptionNotification...");

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
                "test-adapter-01",
                description
        );

        storage.saveNewPhysicalAssetDescriptionNotification(notification);

        logger.info("Test DescriptionNotification: PASSED");
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

        PhysicalRelationshipInstanceVariation variation = new PhysicalRelationshipInstanceVariation(
                System.currentTimeMillis(),
                instance
        );

        storage.savePhysicalAssetRelationshipInstanceCreatedNotification(variation);

        logger.info("Test PhysicalRelationshipInstanceVariation: PASSED");
    }

    // Test to save Digital Action Request Service
    @Test
    public void testDigitalActionRequest() throws StorageException {
        logger.info("Testing DigitalActionRequest...");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("user-role", "admin");
        metadata.put("priority", "high");

        DigitalActionRequest request = new DigitalActionRequest(
                System.currentTimeMillis(),
                "backup-system",
                "full-backup",
                metadata
        );

        storage.saveDigitalActionRequest(request);

        logger.info("Test DigitalActionRequest: PASSED");
    }
}
