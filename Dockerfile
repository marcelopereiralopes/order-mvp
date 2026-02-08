# Estágio de Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
# Pula os testes para agilizar o build
RUN gradle build --no-daemon -x test

# Estágio de Execução
# Trocando openjdk:17-jdk-slim (depreciada) por eclipse-temurin:17-jre (mantida e leve)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]