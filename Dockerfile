FROM openjdk:18
WORKDIR /app
ADD /target/kafka-processor-fatjar.jar /app/kafka-processor.jar
EXPOSE 8080
ENTRYPOINT ["java -Xms$JAVA_XMS -Xmx$JAVA_XMX ", "-jar", "/app/kafka-processor.jar"]