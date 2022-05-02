FROM openjdk:18
WORKDIR /app
ADD /target/kafka-processor-fatjar.jar /app/kafka-processor.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/kafka-processor.jar"]