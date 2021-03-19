# PaymentApi

Payment API Coding Challenge

## Tech Used

* Java 11
* Spring boot
* Postgres
* Cucumber
* Gradle
* Lombok

## Database Commands Ran:

Create Table:
```
CREATE TABLE PAYMENT(
        ID  SERIAL PRIMARY KEY     NOT NULL,
        AMOUNT         BIGINT,
        CURRENCY            VARCHAR(3),
        PRODUCT_NAME        VARCHAR(100)
);
```
Sample Data: 
```
INSERT INTO payment (AMOUNT, CURRENCY, PRODUCT_NAME) values (2000, 'EUR', 'TV');
INSERT INTO payment (AMOUNT, CURRENCY, PRODUCT_NAME) values (3000, 'GBP', 'Nintendo Switch');
INSERT INTO payment (AMOUNT, CURRENCY, PRODUCT_NAME) values (6000, 'USD', 'Car');
```