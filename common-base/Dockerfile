# Stage 1: Build common-base
FROM maven:3-amazoncorretto-23-al2023 AS build

WORKDIR /app

# Sao chép pom.xml và src để build
COPY pom.xml .
COPY src ./src

# Cài đặt common-base vào thư mục repository của Maven
RUN mvn install -DskipTests

