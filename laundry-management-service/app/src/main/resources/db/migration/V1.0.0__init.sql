CREATE TABLE orders
(
  id             INT NOT NULL PRIMARY KEY,
  submitted_time BIGINT,
  estimated_time BIGINT,
  status         VARCHAR(20),
  laundry_id     INT,
  bucket         INT
);

CREATE TABLE laundries_state
(
  id                 SERIAL      NOT NULL PRIMARY KEY,
  version            INT         DEFAULT 0,
  queue_waiting_time BIGINT      DEFAULT 0,
  name               VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE order_submitted_messages
(
  id                    SERIAL NOT NULL PRIMARY KEY,
  order_id              INT    NOT NULL,
  message_status        TEXT   NOT NULL,
  laundry_id            INT    NOT NULL,
  laundry_state_version INT    NOT NULL,
  queue_waiting_time    BIGINT NOT NULL
);

CREATE TABLE order_completed_messages
(
  id                    SERIAL NOT NULL PRIMARY KEY,
  order_id              INT    NOT NULL,
  order_execution_time  BIGINT NOT NULL,
  message_status        TEXT   NOT NULL,
  laundry_id            INT    NOT NULL,
  laundry_state_version INT    NOT NULL,
  queue_waiting_time    BIGINT NOT NULL
)

