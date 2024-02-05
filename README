# Rewards Program API

The Rewards Program API is a Spring Boot application designed to manage a retailer's customer rewards program.

---

## Project Structure

- **HomeworkApplication.java**: Entry point of the application.
- **controller/**: REST controllers for managing customers, rewards, and transactions.
- **entity/**: JPA entities for Customer and Transaction.
- **exception/**: Custom exceptions and global error handling.
- **model/**: Data Transfer Objects (DTOs) and response models.
- **repository/**: Spring Data JPA repositories for entities.
- **service/**: Business logic services for customers, transactions, and rewards.
- **resources/**:
  - **application.properties**: Configuration properties.
  - **data.sql**: Database seeding script.
  - **static/** & **templates/**: (If used) for static content and view templates.

### Information

- Java JDK 17
- Spring Boot 3.2.2
- Using H2 mem database
- Make up data in data.sql
- Api test in /API folder

### Test

1. Use Maven to run test:
   ```shell
   mvn clean test
   ```

## API Endpoints

### Rewards

- **Get Rewards for Customer by ID**
  - `GET /rewards/{customerId}`
  - Retrieves the total reward points earned by a customer across all transactions.

- **Get Rewards for Customer by ID and Period**
  - `GET /rewards/period/{customerId}?startMonth={startMonth}&endMonth={endMonth}`
  - Retrieves the reward points earned by a customer within a specific period.

### Transactions

- **Add a Single Transaction**
  - `POST /transactions`
  - Adds a new transaction record for a customer.

- **Add Multiple Transactions (Batch)**
  - `POST /transactions/batch`
  - Adds multiple transaction records for customers in a batch.

### Customers

- **Add Customer**
  - `POST /customers`
  - Adds a new customer to the rewards program.

- **Get All Customers**
  - `GET /customers`
  - Retrieves a list of all customers in the rewards program.

- **Get Customer By ID**
  - `GET /customers/{customerId}`
  - Retrieves details for a specific customer by their ID.

## Reward Points Calculation

- For each transaction:
  - Earn 1 point for every dollar spent over $50.
  - Earn an additional 2 points for every dollar spent over $100.
- Example: A $120 purchase earns 90 points (2x$20 for the amount over $100 + 1x$50 for the amount over $50).

---
