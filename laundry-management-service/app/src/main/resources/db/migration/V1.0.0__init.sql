CREATE TABLE orders (
  id           SERIAL NOT NULL PRIMARY KEY,
  estimated_time BIGINT,
  status VARCHAR(20),
  laundry_id INT,
  bucket INT
);

CREATE TABLE laundries_state (
  id          SERIAL NOT NULL PRIMARY KEY,
  version     INT,
  queue_waiting_time BIGINT
);

