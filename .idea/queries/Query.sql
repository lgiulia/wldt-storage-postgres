CREATE TABLE digital_twin_state (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    data JSONB NOT NULL
);

CREATE TABLE digital_twin_state_event (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    event_key VARCHAR(255) NOT NULL,
    data JSONB NOT NULL
);

CREATE TABLE physical_asset_event (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    event_key VARCHAR(255) NOT NULL,
    data JSONB NOT NULL
);

CREATE TABLE physical_asset_action_request (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    action_key VARCHAR(255) NOT NULL,
    data JSONB NOT NULL
);

CREATE TABLE physical_asset_property_variation (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    property_key VARCHAR(255) NOT NULL,
    data JSONB NOT NULL
    );

CREATE TABLE physical_asset_description (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    adapter_id VARCHAR(255),
    data JSONB NOT NULL
);

CREATE TABLE physical_relationship_instance_variation (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    instance_key VARCHAR(255) NOT NULL,
    instance_target_id VARCHAR(255),
    relationship_name VARCHAR(255),
    relationship_type VARCHAR(255),
    data JSONB NOT NULL
);

CREATE TABLE digital_action_request (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    action_key VARCHAR(255) NOT NULL,
    data JSONB NOT NULL
);

CREATE INDEX idx_dt_state_timestamp ON digital_twin_state(timestamp);
CREATE INDEX idx_dt_event_timestamp ON digital_twin_state_event(timestamp);
CREATE INDEX idx_pa_event_timestamp ON physical_asset_event(timestamp);
CREATE INDEX idx_pa_action_req_timestamp ON physical_asset_action_request(timestamp);
CREATE INDEX idx_pa_property_var_timestamp ON physical_asset_property_variation(timestamp);
CREATE INDEX idx_pa_desc_timestamp ON physical_asset_description(timestamp);
CREATE INDEX idx_pa_rel_timestamp ON physical_relationship_instance_variation(timestamp);
CREATE INDEX idx_digital_action_timestamp ON digital_action_request(timestamp);