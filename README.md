# Makersharks Supplier Search API


## Overview

Makersharks is developing a proof of concept search API to allow buyers to find manufacturers based on customized criteria. This API enables querying for suppliers by location, nature of business, and manufacturing processes.

## Prerequisites

- **Java 17**: Ensure you have Java 17 installed.
- **Maven 3.8.1 or higher**: Used for building the project.
- **MySQL 8.0 or higher**: Database for storing and querying supplier data.

## Getting Started

### 1. Clone the Repository

Clone the project repository to your local machine:

```bash
git clone https://github.com/Vin-it-9/Search_API.git
```
```bash
CREATE DATABASE makersharks;

```

Configure Application Properties

```bash
spring.application.name=search-api

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/makersharks
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root

# JPA Configuration
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

```bash
mvn clean install
```
```bash
mvn spring-boot:run
```
## Testing the `/api/supplier/query` Endpoint

To test the `/api/supplier/query` endpoint, use the following JSON request bodies. You can paste these into tools like [Postman](https://www.postman.com/) or [cURL](https://curl.se/) to test different scenarios.

### 1. Test Case: Fetch Small Scale Manufacturers in Pune with 3D Printing Capability

**Request:**

```json
{
  "location": "Pune",
  "natureOfBusiness": "small_scale",
  "manufacturingProcesses": "_3d_printing",
  "page": 0,
  "size": 5
}
```

### 2. Test Case: Fetch all manufacturers in Mumbai

**Request:**

```json
{
  "location": "Mumbai",
  "page": 0,
  "size": 5
}
```

### 3. Test Case: Fetch all manufacturers of 1st page size 15

**Request:**

```json
{
  "page": 0,
  "size": 15
}
```
