FROM openjdk:18
WORKDIR /app
ADD /target/kafka-processor-fatjar.jar /app/kafka-processor.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/kafka-processor.jar"]