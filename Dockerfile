FROM openjdk:18
WORKDIR /app
ADD /target/kafka-stream-processor-fatjar.jar /app/kafka-stream-processor.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/kafka-stream-processor.jar"]