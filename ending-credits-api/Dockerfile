FROM openjdk:17-alpine as base
FROM base as builder
WORKDIR /server
COPY . .
RUN ./gradlew build -x test bootJar
FROM base as runner
WORKDIR /server
COPY --from=builder /server/build/libs/ending-credits-api-0.0.1-SNAPSHOT.jar server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]
