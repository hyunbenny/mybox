FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mybox.jar
ENTRYPOINT ["java","-jar","/app.jar"]