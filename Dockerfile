#
# Build stage
#
FROM maven:3.8-openjdk-17 AS build
COPY . .
RUN mvn clean package -Dspring.profiles.active=prod -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/app-0.0.1-SNAPSHOT.jar app.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]