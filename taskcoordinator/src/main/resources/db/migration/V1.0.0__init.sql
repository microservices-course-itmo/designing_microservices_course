CREATE TABLE orders
(
  id             INT NOT NULL PRIMARY KEY,
  laundry_id     INT,
  bucket         INT,
  duration       BIGINT,
  status         VARCHAR(20),
  estimated_time BIGINT,
  completion_time BIGINT
);

CREATE TABLE laundries_state
(
  id                 SERIAL NOT NULL PRIMARY KEY,
  version            INT    DEFAULT 0,
  queue_waiting_time BIGINT DEFAULT 0,
  reserved_time      BIGINT DEFAULT 0
);




