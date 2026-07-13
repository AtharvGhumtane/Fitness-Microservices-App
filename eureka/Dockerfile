# Stage 1: Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml .
RUN chmod +x mvnw
# Download dependencies first (cached layer)
RUN ./mvnw dependency:go-offline -B
COPY src/ src/
RUN ./mvnw package -DskipTests -B

# Stage 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
