FROM openjdk:16-alpine3.13
WORKDIR /app
ADD /target/kafka-stream-processor-fatjar.jar /app/kafka-stream-processor.jar
CMD ["java", "-jar", "/app/kafka-stream-processor.jar"]