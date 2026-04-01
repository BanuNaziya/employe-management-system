-- ============================================================
--  Employee Management System - Database Setup Script
--  Run this script in MySQL before starting the application.
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS ems_db;

-- Step 2: Use the database
USE ems_db;

-- ============================================================
-- NOTE: Hibernate will automatically create the tables below
-- when you run the application (hbm2ddl.auto = update).
-- You only need to run this script to set up the database
-- and insert the default admin user manually if needed.
-- ============================================================

-- Step 3: Create users table (login accounts)
CREATE TABLE IF NOT EXISTS users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role     VARCHAR(20)  NOT NULL          -- 'ADMIN' or 'EMPLOYEE'
);

-- Step 4: Create employees table (employee records)
CREATE TABLE IF NOT EXISTS employees (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) UNIQUE,
    department VARCHAR(50),
    salary     DOUBLE,
    phone      VARCHAR(15),
    user_id    INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ============================================================
-- Default Admin Account
-- Username: admin | Password: admin123
-- (The application also creates this automatically on first run)
-- ============================================================
INSERT IGNORE INTO users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN');

-- ============================================================
-- Sample Data (optional - uncomment to insert test data)
-- ============================================================

-- INSERT INTO employees (name, email, department, salary, phone)
-- VALUES
--   ('Alice Johnson', 'alice@example.com',   'IT',      75000, '9876543210'),
--   ('Bob Smith',     'bob@example.com',     'HR',      55000, '9123456780'),
--   ('Carol Davis',   'carol@example.com',   'Finance', 65000, '9988776655'),
--   ('David Lee',     'david@example.com',   'IT',      80000, '9001122334'),
--   ('Eva Martinez',  'eva@example.com',     'HR',      52000, '9445566778');
