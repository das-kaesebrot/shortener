FROM openjdk:11-jdk-slim
RUN addgroup --system spring && adduser --system --group spring
USER spring:spring
ARG JAR_FILE=build/libs/shortener-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]