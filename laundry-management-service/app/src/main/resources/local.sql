DROP DATABASE IF EXISTS user_management_db;
DROP USER IF EXISTS user_management_db_user;
CREATE USER user_management_db_user WITH ENCRYPTED PASSWORD '123';
CREATE DATABASE user_management_db WITH OWNER user_management_db_user;