DROP DATABASE IF EXISTS task_coordinator_db;
DROP USER IF EXISTS task_coordinator_db_user;
CREATE USER task_coordinator_db_user WITH ENCRYPTED PASSWORD '123';
CREATE DATABASE task_coordinator_db WITH OWNER task_coordinator_db_user;
