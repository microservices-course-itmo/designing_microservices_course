CREATE SCHEMA IF NOT EXISTS tariff_management;

CREATE TABLE tariff_management.tariffs
(
    id           SERIAL NOT NULL PRIMARY KEY,
    name         VARCHAR(30) NOT NULL,
    price        NUMERIC(6, 2),
    washing_time BIGINT
);