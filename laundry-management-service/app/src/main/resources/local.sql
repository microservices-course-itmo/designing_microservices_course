DROP DATABASE IF EXISTS laundry_db;
DROP USER IF EXISTS laundry_db_user;
CREATE USER laundry_db_user WITH ENCRYPTED PASSWORD '123';
CREATE DATABASE laundry_db WITH OWNER laundry_db_user;