 --MySQL query for DataBase

create database bankdb;

use bankdb;

-- Create 'users' table
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- Create 'accounts' table
CREATE TABLE accounts (
    account_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    account_name VARCHAR(100) NOT NULL,
    account_balance DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (account_id),
    INDEX (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create 'loans' table
CREATE TABLE loans (
    loan_id INT NOT NULL AUTO_INCREMENT,
    account_id INT NOT NULL,
    contact VARCHAR(100) NOT NULL,
    loan_amount DECIMAL(10,2) NOT NULL,
    loan_time INT NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL DEFAULT 5.00,
    loan_status VARCHAR(50) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (loan_id),
    INDEX (account_id),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Create 'loan_updates' table
CREATE TABLE loan_updates (
    loan_id INT NOT NULL,
    updated_loan_amount DECIMAL(10,2) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (loan_id),
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);



