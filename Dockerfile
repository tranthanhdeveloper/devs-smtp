FROM amazoncorretto:11.0.17
EXPOSE 5025 8089
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]