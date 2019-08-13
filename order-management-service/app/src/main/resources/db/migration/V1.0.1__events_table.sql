CREATE TABLE ordermanagement_events
(
    id           SERIAL NOT NULL PRIMARY KEY,
    created_time BIGINT,
    event_type   TEXT,
    event_status TEXT,
    message      BYTEA
);