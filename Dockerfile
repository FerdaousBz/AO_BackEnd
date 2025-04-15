# Use a base image with Java and Maven pre-installed
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the application source code
COPY src ./src

# Build the Spring Boot application
RUN mvn package -DskipTests

# Debugging: List the contents of the target directory
RUN ls -la target

# Create the final Docker image with the compiled application
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar

# Expose the port your Spring Boot application listens on
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
