# Receipt Processor API

This is a Kotlin-based Spring Boot application that processes receipts and calculates points based on specific business rules. The application provides RESTful endpoints to process receipts, calculate points, and retrieve the points awarded for a specific receipt.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Business Rules](#business-rules)
- [Testing](#testing)

## Features

- **Receipt Processing:** Processes a receipt and calculates points based on predefined rules.
- **Points Retrieval:** Retrieve the points awarded to a processed receipt.
- **In-Memory Storage:** Receipts and their corresponding points are stored in-memory.

## Requirements

- **JDK 22 or higher**
- **Gradle** (or use the provided Gradle Wrapper)
- **Docker** (optional, for containerized deployment)

## Installation

### Clone the Repository

```bash
git clone https://github.com/Dtsonev72/receipt-processor-api.git
cd receipt-processor-api
```

### Build the project

You can build the project using the Gradle Wrapper
```bash 
./gradlew clean build
```
or in Windows:
```cmd
gradlew clean build
```

### Running the application
#### Locally
You can run the application using Gradle:
```bash
./gradlew bootRun
```
The application will start on http://localhost:8080

#### Docker

To run the application in a Docker container:

1. Build the Docker image:
    ```bash
    docker build -t receipt-processor-api .
   ```
2. Run the Docker container:
    ```bash
    docker run -p 8080:8080 receipt-processor-api
    ```

### Usage
#### Process a Receipt

To process a receipt and calculate points, send a POST request to the /receipts/process endpoint with the receipt details in JSON format.

#### Retrieve Points

To retrieve the points awarded for a specific receipt, send a GET request to the /receipts/{id}/points endpoint, where {id} is the receipt ID returned from the /receipts/process request.

#### API Endpoints
POST ```/receipts/process```

Description: Processes a receipt and calculates points.

Request Body:
```json
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },
    {
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    }
  ],
  "total": "35.35"
}
```

Response:
```json
{
  "id": "7fb1377b-b223-49d9-a31a-5a02701dd310"
}
```

GET ```/receipts/{id}/points```

Description: Retrieves the points awarded for a specific receipt.

Response:

```json
{
  "points": 28
}

```

### Business Rules

The points are calculated based on the following rules:

* One point for every alphanumeric character in the retailer name.
* 50 points if the total is a round dollar amount with no cents.
* 25 points if the total is a multiple of `0.25`.
* 5 points for every two items on the receipt.
* If the trimmed length of the item description is a multiple of 3, multiply the price by `0.2` and round up to the nearest integer. The result is the number of points earned.
* 6 points if the day in the purchase date is odd.
* 10 points if the time of purchase is after 2:00pm and before 4:00pm.

### Testing
#### Running unit test cases
To run the unit tests, use the following command:
```bash
./gradlew test
```

#### Testing with Docker
If you're using Docker, you can also run tests inside a Docker container:
```bash
docker build -t receipt-processor-api .
docker run receipt-processor-api ./gradlew test
```