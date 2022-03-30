package szoeke.bence.kafkastreams.processor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.VoidDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MyConsumer {

    private static final String INPUT_TOPIC = "streams-input";
    private static final String OUTPUT_TOPIC = "streams-output";
    private final Properties properties;

    public MyConsumer() {
        properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "MyConsumerGroup");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    }

    public void consumeRecords() {
        try (KafkaConsumer<Void, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Collections.singleton(INPUT_TOPIC));
            doConsuming(consumer);
        }
    }

    private void doConsuming(KafkaConsumer<Void, String> consumer) {
        while (true) {
            ConsumerRecords<Void, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<Void, String> record : records) {
                System.out.println(record.timestamp());
            }
        }
    }
}
