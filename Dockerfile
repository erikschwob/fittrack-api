# --- Build stage: compile and package with the Maven wrapper ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Resolve dependencies first so they cache independently of source changes.
COPY pom.xml .
RUN mvn -B dependency:go-offline -q

COPY src ./src
RUN mvn -B clean package -DskipTests -q

# --- Runtime stage: slim JRE, non-root, with a JVM tuned for containers ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Run as an unprivileged user.
RUN useradd --system --uid 10001 fittrack
USER fittrack

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Spring Boot Actuator exposes a readiness probe; container orchestrators use it.
HEALTHCHECK --interval=15s --timeout=3s --start-period=40s --retries=5 \
    CMD ["sh", "-c", "wget -qO- http://localhost:8080/actuator/health/readiness | grep -q UP || exit 1"]

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
