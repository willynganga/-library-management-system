# Use a base image with Java 17 and Gradle pre-installed
FROM amazoncorretto:17-alpine-jdk AS build

# Set the working directory to /app
WORKDIR /app

# Copy the Gradle files
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test

# Create a new image with Java 17
FROM amazoncorretto:17-alpine-jdk

# Set the working directory to /app
WORKDIR /app

# Copy the built JAR file from the previous build stage
COPY --from=build /app/build/libs/library-management-system-0.0.1-SNAPSHOT.jar ./

# Specify the JVM arguments for the application
ENTRYPOINT ["java", "--add-opens", "java.base/java.time=ALL-UNNAMED", "-jar", "library-management-system-0.0.1-SNAPSHOT.jar"]
