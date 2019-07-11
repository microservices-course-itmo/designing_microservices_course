CREATE SCHEMA IF NOT EXISTS user_management;

CREATE TABLE user_management.users
(
  id             SERIAL NOT NULL PRIMARY KEY,
  login          TEXT,
  status_card    TEXT
);
