CREATE SCHEMA IF NOT EXISTS account_management;

CREATE TABLE account_management.payments
(
  payment_id     SERIAL        NOT NULL PRIMARY KEY,
  amount         NUMERIC(6, 2) NOT NULL,
  username       TEXT          NOT NULL,
  payment_status VARCHAR(20)   NOT NULL
);