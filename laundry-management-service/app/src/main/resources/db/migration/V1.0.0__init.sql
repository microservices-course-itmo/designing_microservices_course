CREATE SCHEMA IF NOT EXISTS laundry_management;

CREATE TABLE laundry_management.orders
(
  id             INT NOT NULL PRIMARY KEY,
  submitted_time BIGINT,
  estimated_time BIGINT,
  status         VARCHAR(20),
  laundry_id     INT,
  bucket         INT
);

CREATE TABLE laundry_management.laundries_state
(
  id                 SERIAL      NOT NULL PRIMARY KEY,
  version            INT         DEFAULT 0,
  queue_waiting_time BIGINT      DEFAULT 0,
  name               VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE laundry_management.laundry_events
(
    id           SERIAL NOT NULL PRIMARY KEY,
    created_time BIGINT,
    event_type   TEXT,
    event_status TEXT,
    message      BYTEA
);


