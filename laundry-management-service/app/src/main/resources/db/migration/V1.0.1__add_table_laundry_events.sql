CREATE TABLE laundry_events
(
  id             SERIAL NOT NULL PRIMARY KEY,
  date           BIGINT,
  event_type     TEXT,
  message_status TEXT,
  message        BYTEA
)

