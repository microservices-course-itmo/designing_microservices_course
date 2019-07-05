CREATE SCHEMA IF NOT EXISTS tariff_management;

CREATE TABLE tariff_management.tariffs
(
    id           INT NOT NULL PRIMARY KEY,
    name         INT NOT NULL,
    price        NUMERIC(6, 2),
    washing_time BIGINT
);