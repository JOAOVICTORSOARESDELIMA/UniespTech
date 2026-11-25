# ---- Etapa 1: build com Maven (roda os testes) ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B package

# ---- Etapa 2: imagem final só com o JRE e o .jar ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app && mkdir -p /app/dados && chown app:app /app/dados
COPY --from=build /app/target/sistema-academico.jar app.jar
USER app

ENV DATA_FILE=/app/dados/alunos.csv \
    HEALTH_PORT=8080
VOLUME ["/app/dados"]
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s \
  CMD wget -qO- http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
