# Operations API

## Description
The Operations API manages mathematical operations, including cost deduction and user balance verification. It is part of a microservices system and depends on the Authentication API to validate users.

## Technologies Used
- **Java 17**
- **Spring Boot 3.3.4**: Framework for building REST applications.
- **JUnit**: For unit testing the API's functionalities.
- **Maven**: Build and dependency management.

## Default Port
The API starts on **port 8082** by default.

## Features
- **Mathematical Operations**: Performs calculations, deducting values from user balance.
- **Balance Verification**: Checks and validates user balance before performing operations.

## Main Endpoints
- `POST /api/operations`: Performs a mathematical operation.
- `GET /api/balance`: Checks the current balance of a user.

## Installation
1. Clone the repository.
2. In the `operations` directory, run:
   ```bash
   ./mvnw clean install

Start the API with:
   ./mvnw spring-boot:run

Unit Tests
   The API includes unit tests implemented with JUnit to validate key functionalities, including:
   .Execution of mathematical operations.
   .Verification and updating of user balance.

To run the tests:
 ./mvnw test
