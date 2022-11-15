FROM openjdk:18
WORKDIR /app
ADD /target/kafka-processor-fatjar.jar /app/kafka-processor.jar
ADD jmx/jmx_prometheus_javaagent-0.17.0.jar /app/jmx_prometheus_javaagent.jar
ADD jmx/jmx_config.yaml /app/jmx_config.yaml

ENV JMXPORT 12345
ENV HEAPMIN 1024m
ENV HEAPMAX 1024m

EXPOSE 8080
EXPOSE ${JMXPORT}

RUN echo "keytool -noprompt -keystore cluster.truststore.jks -storepass 123456 -alias CARoot -import -file /app/cluster-ca-cert/ca.crt" > /app/startup.sh && echo "##############Starting Data Generator################" >> /app/startup.sh && echo "java -Xms${HEAPMIN} -Xmx${HEAPMAX} -javaagent:/app/jmx_prometheus_javaagent.jar=0.0.0.0:${JMXPORT}:/app/jmx_config.yaml -jar /app/kafka-processor.jar" >> /app/startup.sh

ENTRYPOINT /bin/bash /app/startup.sh