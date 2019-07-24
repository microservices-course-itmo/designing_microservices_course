CREATE SCHEMA IF NOT EXISTS account_management;

CREATE TABLE account_management.payments
(
  id            INT           NOT NULL PRIMARY KEY,
  amount        NUMERIC(6, 2) NOT NULL,
  userName      TEXT UNIQUE   NOT NULL,
  paymentStatus VARCHAR(20)   NOT NULL
);