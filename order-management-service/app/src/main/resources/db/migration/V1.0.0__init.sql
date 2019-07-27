CREATE SCHEMA IF NOT EXISTS order_management;

CREATE TABLE order_management.order_details
(
    id        INT NOT NULL PRIMARY KEY,
    order_id  INT NOT NULL,
    weight    INT NOT NULL,
    tariff_id INT,
    price     NUMERIC(6, 2)
);

CREATE TABLE order_management.orders
(
    id             SERIAL      NOT NULL PRIMARY KEY,
    username       VARCHAR(20) NOT NULL,
    created_time   BIGINT,
    estimated_time BIGINT,
    status         VARCHAR(20),
    total_price    NUMERIC(8, 2)
);

CREATE TABLE order_management.pending_details
(
    id           SERIAL NOT NULL PRIMARY KEY,
    weight       INT,
    created_time BIGINT
);
