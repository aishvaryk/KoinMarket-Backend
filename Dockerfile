#
# Build stage
#
FROM 3.8.7-openjdk-17 AS build
COPY . .
RUN mvn clean package -Dspring.profiles.active=prod -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]