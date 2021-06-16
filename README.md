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
Request:
```bash
HTTP/1.1 GET /statistics
```
Response:
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
Request:
```bash
HTTP/1.1 POST /transactions
Accept: application/json
Content-Type: application/json

{
    "amount": "2.2343",
    "timestamp": "2021-06-14T17:56:03.312Z"
}
```
Response:
```bash
HTTP/1.1 200 OK
```
### Clear Transaction
Request:
```bash
HTTP/1.1 DELETE /transactions
```
Response:
```bash
HTTP/1.1 204 NO_CONTENT
```
