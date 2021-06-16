# challenge-transactions-statistics

Spring-boot Microservice that provides API to:
 - Display statistics of transactions that occured in last 60 seconds.
 - Save transaction (amount, timestamp)
 - Clear all previous transactions

## Installation
Use maven to compile and build project:
```bash
mvn clean install 
```
Run the spring-boot application:
```bash
mvn spring-boot:run
```

## Usage

### Get statistics
This endpoint returns the statistics based on the transactions that happened in the last 60 seconds.

The response body contains:
 - sum : a BigDecimal specifying the total sum of transaction value in the last 60 seconds
 - avg : a BigDecimal specifying the average amount of transaction value in the last 60 seconds
 - max : a BigDecimal specifying single highest transaction value in the last 60 seconds
 - min : a BigDecimal specifying single lowest transaction value in the last 60 seconds
 - count : a long specifying the total number of transactions that happened in the last 60 seconds

**Request**:
```bash
HTTP/1.1 GET /statistics
```
**Response**:
```bash
HTTP/1.1 200 OK

{
    "sum": "94.47",
    "avg": "47.23",
    "max": "92.23",
    "min": "2.23",
    "count": 2
}
```

### Post Transaction
This endpoint is called to create a new transaction.

Following details need to be specified in the request body:
 - amount : transaction amount; a string of arbitrary length that is parsable as a BigDecimal
 - timestamp : transaction time in the ISO 8601 format

**Request**:
```bash
HTTP/1.1 POST /transactions
Accept: application/json
Content-Type: application/json

{
    "amount": "2.2343",
    "timestamp": "2021-06-14T17:56:03.312Z"
}
```
**Response**:
```bash
HTTP/1.1 201 CREATED
```

### Clear Transaction
This endpoint causes all existing transactions to be deleted

**Request**:
```bash
HTTP/1.1 DELETE /transactions
```
**Response**:
```bash
HTTP/1.1 204 NO_CONTENT
```
