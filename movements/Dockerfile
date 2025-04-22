# Etapa de construcción
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copiar los archivos de configuración y código fuente
COPY build.gradle settings.gradle ./
COPY src ./src

# Compilar el proyecto omitiendo los tests
RUN gradle build -x test

# Etapa de ejecución
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
COPY --from=build /app/build/libs/*.jar /app/movements.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/movements.jar"]
