# Stage 1: build with Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom y wrapper para aprovechar cache de dependencias
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Copiamos el código fuente
COPY src src

# Ejecutamos el build (se empaqueta el jar)
RUN ./mvnw -B -DskipTests package

# Stage 2: runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR generado desde el stage de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
