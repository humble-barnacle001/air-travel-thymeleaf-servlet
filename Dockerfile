# Build stage #
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package stage #
FROM openjdk:8-jre-slim
COPY --from=build /home/app/target/air.war /home/air.war
COPY --from=build /home/app/target/dependency/webapp-runner.jar /home/deps/webapp-runner.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/deps/webapp-runner.jar", "--port", "8080", "/home/air.war"]