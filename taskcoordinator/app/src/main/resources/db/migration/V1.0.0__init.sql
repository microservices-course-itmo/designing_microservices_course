CREATE SCHEMA IF NOT EXISTS ${schema};

CREATE TABLE laundries_state
(
    id                 SERIAL NOT NULL PRIMARY KEY,
    version            INT    DEFAULT 0,
    queue_waiting_time BIGINT DEFAULT 0,
    reserved_time      BIGINT DEFAULT 0
);

CREATE TABLE orders
(
    id              INT NOT NULL PRIMARY KEY,
    laundry_id      INT REFERENCES laundries_state (id),
    bucket          INT,
    duration        BIGINT,
    status          VARCHAR(20),
    estimated_time  BIGINT,
    completion_time BIGINT
);

CREATE TABLE details
(
    id INT NOT NULL PRIMARY KEY,
    weight INT NOT NULL,
    duration BIGINT NOT NULL,
    order_id INT NOT NULL REFERENCES orders(id)
);