FROM maven:3.8.7-eclipse-temurin-17 as builder
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
COPY .env .env
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/*jar
COPY src/main/resources/ /app/resources/
ENTRYPOINT ["java","-jar","/opt/app/*jar"]