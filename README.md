# Library Management APIs
The back-end repo for Library Management API's

## Requirements
- Java 17+
- Postgres DB
- Your favorite IDE
- Postman for API testing

## Clone
```
git clone https://github.com/willynganga/library-management-system.git
```

## Build
To build the project:
- On Linux or MacOS Terminal
```
./gradlew build
```
- On Windows CMD or Powershell
```
./gradlew.bat build
```

## Run
To run the project, please make sure to have built the project first for it to set up the necessary dependencies.
- On Linux or MacOS Terminal
```
./gradlew bootRun
```
- On Windows CMD or Powershell
```
./gradlew.bat bootRun
```

## Environment Variables
These are required to run the application.
```
JWT_SECRET="Your jwt secret"
JWT_EXPIRATION_DURATION="Jwt expiration in milliseconds (eg. 3600000ms = 1hr)"
POSTGRES_URL="Your Postgres db url"
POSTGRES_PASSWORD="Your db password"
POSTGRES_USERNAME="Your db username"
```

## API Documentation (Postman)
I have documented the APIs in a Postman collection added to this repo `library-management-apis-postman.json`. You can import it for ease of use. Please note that I have appended a `v1` prefix to the apis and each of them start with `/api/v1/`. The Postman collection has already defined some variables and all you need to do is change them to your needs.

## Authentication
I have added Jwt authentication. You can find the registration and login endpoints in the postman collection. Once you login, the token is available for all the requests that require it via a `{{token}}` collection variable.
