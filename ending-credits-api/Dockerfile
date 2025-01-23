FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/ending-credits-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/server.jar"]