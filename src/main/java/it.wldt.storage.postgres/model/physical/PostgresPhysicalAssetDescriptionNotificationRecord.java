package it.wldt.storage.postgres.model.physical;

import it.wldt.adapter.physical.PhysicalAssetDescription;

public class PostgresPhysicalAssetDescriptionNotificationRecord {
    // Timestamp of the notification
    private long notificationTimestamp;

    // Adapter ID associated to the notification
    private String adapterId;

    // Physical Asset Description associated to the notification
    private PhysicalAssetDescription physicalAssetDescription;

    // Default constructor
    public PostgresPhysicalAssetDescriptionNotificationRecord() {
    }

    /* Constructors of physical asset description notification record
    @param notificationTImestamp  TImestamp of the notification
    @param adapterId  ID of the adapter associated with the notification
    @param physicalAssetDescription  Description of the physical asset associated with the notification
     */
    public PostgresPhysicalAssetDescriptionNotificationRecord(long notificationTimestamp, String adapterId, PhysicalAssetDescription physicalAssetDescription) {
        this.notificationTimestamp = notificationTimestamp;
        this.adapterId = adapterId;
        this.physicalAssetDescription = physicalAssetDescription;
    }

    // Getter and Setter

    public long getNotificationTimestamp() {
        return notificationTimestamp;
    }

    public void setNotificationTimestamp(long notificationTimestamp) {
        this.notificationTimestamp = notificationTimestamp;
    }

    public String getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(String adapterId) {
        this.adapterId = adapterId;
    }

    public PhysicalAssetDescription getPhysicalAssetDescription() {
        return physicalAssetDescription;
    }

    public void setPhysicalAssetDescription(PhysicalAssetDescription physicalAssetDescription) {
        this.physicalAssetDescription = physicalAssetDescription;
    }
}
