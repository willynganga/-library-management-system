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
