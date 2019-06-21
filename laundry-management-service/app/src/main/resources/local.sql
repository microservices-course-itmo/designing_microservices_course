DROP DATABASE IF EXISTS laundry_management_db;
DROP USER IF EXISTS laundry_management_db_user;
CREATE USER laundry_management_db_user WITH ENCRYPTED PASSWORD '123';
CREATE DATABASE laundry_management_db WITH OWNER user_management_db_user;