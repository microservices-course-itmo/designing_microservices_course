CREATE SCHEMA IF NOT EXISTS task_coordinator;

CREATE TABLE task_coordinator.laundries_state
(
    id                 SERIAL NOT NULL PRIMARY KEY,
    version            INT    DEFAULT 0,
    queue_waiting_time BIGINT DEFAULT 0,
    reserved_time      BIGINT DEFAULT 0
);

CREATE TABLE task_coordinator.orders
(
  id             INT NOT NULL PRIMARY KEY,
  laundry_id     INT REFERENCES task_coordinator.laundries_state(id),
  bucket         INT,
  duration       BIGINT,
  status         VARCHAR(20),
  estimated_time BIGINT,
  completion_time BIGINT
);

CREATE TABLE task_coordinator.details
(
    id INT NOT NULL PRIMARY KEY,
    weight INT NOT NULL,
    duration BIGINT NOT NULL,
    order_id INT NOT NULL REFERENCES task_coordinator.orders(id)
);