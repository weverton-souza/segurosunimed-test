# First stage: build the application with Gradle
FROM maven:3.8.4-openjdk-11-slim AS builder

# Copy files and the 'src' and 'config' folders to the temporary image
WORKDIR /usr/src/seguro-unimed-test
COPY . .

# Build the Spring Boot application and generate the JAR file
RUN mvn clean package -DskipTests

# Second stage: create the final image
FROM openjdk:11-ea-9-jdk AS server

WORKDIR /usr/src/seguro-unimed-test

# Expose the application port for external access
EXPOSE 8080

# Copy the JAR generated in the first stage to the final image

COPY --from=builder "/usr/src/seguro-unimed-test/target/*.jar" ./seguro-unimed-application.jar

# Start the application with the specified Java command
# The '-Dspring.profiles.active=local' argument sets the active profile to 'local' >> If necessary!
CMD ["java", "-jar", "./seguro-unimed-application.jar"]
