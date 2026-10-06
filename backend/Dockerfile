FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/target/beam-calculator-0.0.1-SNAPSHOT.jar beamcalc.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/beamcalc.jar"]
