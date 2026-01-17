# Banking API - Customer Management System

A Spring Boot REST API for managing banking customers with full CRUD operations. This project demonstrates best practices in API development, including proper exception handling, pagination, data validation, and comprehensive unit testing.

## Project Overview

This is a professional REST API built with Spring Boot that manages customer data for a banking application. It includes:

- Full CRUD operations (Create, Read, Update, Delete)
- Pagination support for retrieving customers
- Global exception handling
- H2 in-memory database
- Unit and integration tests with Mockito
- Input validation with Jakarta Bean Validation
- REST API best practices

## Technologies Used

- **Java 17** - Programming language
- **Spring Boot 4.0.1** - Framework
- **Spring Data JPA** - ORM and database operations
- **H2 Database** - In-memory database for development and testing
- **Lombok** - Boilerplate code reduction
- **ModelMapper** - Object mapping between entities and DTOs
- **JUnit 5 & Mockito** - Unit testing framework
- **Maven** - Build tool

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Postman (for API testing)

## Getting Started

### 1. Clone & Setup

```bash
git clone https://github.com/CadenEbert/BankingApi.git
cd BankingApi/app-crud
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=CustomerServiceImpTest
```

## Database

H2 in-memory database auto-initializes with the `customers` table:

```sql
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20)
);
```

### H2 Console
Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

## API Endpoints

### Base URL: `http://localhost:8080/api`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/admin/customers` | Create a new customer |
| GET | `/public/customers` | Get all customers (paginated) |
| PUT | `/public/customers/{customerId}` | Update customer |
| DELETE | `/admin/customers/{customerId}` | Delete customer |

## Testing with Postman

### 1. Create Customer (POST)

**URL:** `POST http://localhost:8080/api/admin/customers`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "555-0101"
}
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "555-0101"
}
```

---

### 2. Get All Customers (GET)

**URL:** `GET http://localhost:8080/api/public/customers?pageNumber=0&pageSize=10`

**Expected Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phoneNumber": "555-0101"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1
}
```

---

### 3. Get Customer by ID (GET)

**URL:** `GET http://localhost:8080/api/public/customers/1`

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "555-0101"
}
```

---

### 4. Update Customer (PUT)

**URL:** `PUT http://localhost:8080/api/public/customers/1`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phoneNumber": "555-0102"
}
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phoneNumber": "555-0102"
}
```

---

### 5. Delete Customer (DELETE)

**URL:** `DELETE http://localhost:8080/api/admin/customers/1`

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "555-0101"
}
```

---

## Query Parameters

### Pagination

When retrieving all customers, use pagination to limit results:

```
GET /api/public/customers?pageNumber=0&pageSize=5
```

| Parameter | Type | Description |
|-----------|------|-------------|
| pageNumber | int | Page index (0-based) |
| pageSize | int | Number of records per page |

## Error Handling

The API returns appropriate HTTP status codes:

| Status | Meaning |
|--------|---------|
| 200 | Success |
| 400 | Bad Request (validation error) |
| 404 | Customer Not Found |
| 409 | Conflict (duplicate customer) |

**Example Error Response (404):**
```json
{
  "message": "Customer not found"
}
```

## Project Structure

```
src/
├── main/
│   ├── java/com/bank/app/
│   │   ├── AppMainApplication.java
│   │   ├── Customer.java (Entity)
│   │   ├── controller/
│   │   │   └── CustomerController.java
│   │   ├── service/
│   │   │   ├── CustomerService.java (Interface)
│   │   │   └── CustomerServiceImp.java (Implementation)
│   │   ├── repository/
│   │   │   └── CustomerRepository.java
│   │   ├── payload/
│   │   │   ├── CustomerDTO.java
│   │   │   ├── CustomerResponse.java
│   │   │   └── APIResponse.java
│   │   ├── exceptions/
│   │   │   ├── ResourceNotFoundException.java
│   │   │   ├── APIException.java
│   │   │   └── MyGlobalExceptionHandler.java
│   │   └── config/
│   │       └── AppConfig.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/bank/app/
    │   └── service/
    │       └── CustomerServiceImpTest.java
    └── resources/
        └── test-data.sql
```

## Key Features Demonstrated

### REST API Design
- Proper HTTP methods and status codes
- RESTful endpoint naming conventions
- Data Transfer Objects (DTOs) for API contracts

### Spring Boot Best Practices
- Dependency injection with @Autowired
- Separation of concerns with Controller → Service → Repository layers
- Global exception handling with @RestControllerAdvice

### Database
- JPA entity mapping with Lombok annotations
- Repository pattern with Spring Data
- Pagination support for large datasets

### Testing
- Unit tests with Mockito for mocking dependencies
- Assertion testing with AssertJ for readable test assertions
- Proper test naming conventions for clarity

## Security Notes

- Endpoints use `/admin` and `/public` prefixes for future authentication integration
- Input validation with Jakarta Bean Validation
- Exception handling prevents sensitive information leakage




