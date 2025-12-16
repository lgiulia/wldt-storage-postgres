package it.wldt.storage.postgres.model.common;

// Represents the generic configuration for postgresql WLDT storage
public class PostgresWldtStorageConfiguration {
    private final String configurationId;

    // Constructs a WldtStrorageConfiguration with an Id
    public PostgresWldtStorageConfiguration(String configurationId) {
        this.configurationId = configurationId;
    }

    // Getter
    public String getConfigurationId() {
        return configurationId;
    }
}