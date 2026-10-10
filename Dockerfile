# Multi-stage Docker build for MubeenMart (Anna University R2025)
# Stage 1: Build the deployable WAR archive using Maven & OpenJDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Compile sources and package web application archive
COPY src ./src
COPY db ./db
RUN mvn clean package -DskipTests

# Stage 2: Production Servlet Container on Apache Tomcat 9
FROM tomcat:9.0.86-jdk17-temurin
LABEL maintainer="Mohamed Mubeen <mrminecraftpc1@gmail.com>"
LABEL description="MubeenMart - Multi-Seller E-Commerce Marketplace"

# Remove default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy MubeenMart as default ROOT context
COPY --from=builder /build/target/mubeenmart.war /usr/local/tomcat/webapps/ROOT.war

# Expose default HTTP port
EXPOSE 8080

# Support dynamic cloud PaaS port assignment (e.g. Render, Railway) via $PORT
CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT:-8080}\\\"/g\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]
