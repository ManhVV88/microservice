# Stage 1: build
# Start with a Maven image that includes JDK 23 extend common-base image in docker local
FROM common-base:0.0.1 AS build

# Copy source code and pom.xml file to /app folder
FROM build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with maven
RUN mvn package -DskipTests

#Stage 2: create image
# Start with Amazon Correto JDK 23
FROM amazoncorretto:23-al2023

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]