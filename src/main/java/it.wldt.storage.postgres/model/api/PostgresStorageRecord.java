package it.wldt.storage.postgres.model.api;

// Basic class that represents a generic storage record for PostgreSQL
public class PostgresStorageRecord {
    private Long id;

    // Default constructor
    public PostgresStorageRecord() {
    }

    /* Constructor to inizialize ID
    @param id  ID of the instance
     */
    public PostgresStorageRecord(Long id) {
        this.id = id;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PostgresStorageRecord [id=" + id + "]";
    }
}
