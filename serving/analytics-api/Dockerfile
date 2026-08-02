FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8088

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]