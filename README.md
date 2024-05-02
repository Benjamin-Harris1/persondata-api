# Person Data API

## Overview
Person Data API is a microservice-based application that uses data from various external microservices to provide combined information about a person's age, gender, and nationality based on their name.

## Features
- Fetch nationality data using nationalize.io API
- Fetch gender data using genderize.io API and the country code from the nationalize response
- Fetch age data using agify.io API and the country code from the nationalize response

## Pre-requisites
- JDK 17

## Running the application
1. Clone the repository
2. Navigate to the project directory
3. Run the application
```
./mvnw spring-boot:run
```

## Usage
- Send a GET request to 
```
http://localhost:8080/persondata?firstName={firstName}&middleName={middleName}&lastName={lastName}
```
- Replace `{firstName}`, `{middleName}`, and `{lastName}` with the person's first name, middle name, and last name respectively, middle name is optional.
- The response will look like this:
```
{
    "fullName": "Benjamin Harris",
    "firstName": "Benjamin",
    "middleName": null,
    "lastName": "Harris",
    "gender": "male",
    "genderProbability": 1.0,
    "age": 44,
    "ageProbability": 1.0,
    "country": "US",
    "countryProbability": 0.28
}
```