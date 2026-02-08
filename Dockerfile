# Estágio de Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
# Pula os testes para agilizar o build no free tier (opcional, mas recomendado se tiver testes de integração pesados)
RUN gradle build --no-daemon -x test

# Estágio de Execução
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]