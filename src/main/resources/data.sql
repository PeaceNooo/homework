-- Drop tables if they already exist to start fresh
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS customer;

-- Create customer table
CREATE TABLE customer (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(255) NOT NULL
);

-- Create transaction table
CREATE TABLE transaction (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     amount DECIMAL(19,2),
     transaction_date DATE,
     customer_id BIGINT,
     FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Insert customers
INSERT INTO customer (name) VALUES ('John Doe');
INSERT INTO customer (name) VALUES ('Jane Jose');
INSERT INTO customer (name) VALUES ('Alice Smith');
INSERT INTO customer (name) VALUES ('Bob Johnson');

-- Assuming the customers get IDs 1, 2, 3, 4 respectively
-- Insert transactions for John Doe
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (100.00, '2023-01-15', 1);
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (150.50, '2023-01-20', 1);

-- Insert transactions for Jane Doe
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (200.75, '2023-02-05', 2);
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (50.25, '2023-02-10', 2);

-- Insert transactions for Alice Smith
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (300.40, '2023-03-12', 3);
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (450.00, '2023-03-19', 3);

-- Insert transactions for Bob Johnson
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (500.00, '2023-04-25', 4);
INSERT INTO transaction (amount, transaction_date, customer_id) VALUES (750.20, '2023-04-30', 4);

