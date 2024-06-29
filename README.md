
# Bank Management System

## Demo
https://github.com/Vin-it-9/bank-system/assets/148773020/28bfa14f-9064-459d-93a3-94a2099c7d4c


## Tech Stack

**Client:** HTML, TailwindCSS 

**Server:** java Servlet , JSP 

**Database:** MySQL 

## Mysql Database

MySQL query for tables
```bash
create database bankdb;
```
```bash
use bankdb;
```
```bash
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
```

```bash
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
```

```bash
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
```
```bash
CREATE TABLE loan_updates (
    loan_id INT NOT NULL,
    updated_loan_amount DECIMAL(10,2) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (loan_id),
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);
```

## Screenshots

![Screenshot 2024-06-29 184611](https://github.com/Vin-it-9/bank-system/assets/148773020/36da830b-4214-4741-a257-7a5d96a03aaf)

![Screenshot 2024-06-29 184721](https://github.com/Vin-it-9/bank-system/assets/148773020/3baa5262-f07d-4a2f-b865-d5c173054a28)

![Screenshot 2024-06-29 184641](https://github.com/Vin-it-9/bank-system/assets/148773020/68b7bde0-d70c-47dc-a01c-382145a4ad74)

![Screenshot 2024-06-29 184655](https://github.com/Vin-it-9/bank-system/assets/148773020/3b5e5ffe-45ca-4f2e-b5cd-ce1e5bbcf873)




