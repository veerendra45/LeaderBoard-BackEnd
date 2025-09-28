#FROM openjdk:17-jdk-alpine
#
#WORKDIR /app
#
#COPY target/LeaderBoard-BackEnd-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#
#CMD [ "java", "-jar", "app.jar" ]
#

# Use a Maven + JDK image to build
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use a smaller JDK image to run
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/LeaderBoard-BackEnd-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
