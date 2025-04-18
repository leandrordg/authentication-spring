FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

COPY src/main/resources/app.key /app/app.key
COPY src/main/resources/app.pub /app/app.pub

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
