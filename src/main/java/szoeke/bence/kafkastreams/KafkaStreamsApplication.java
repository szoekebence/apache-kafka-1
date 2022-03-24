package szoeke.bence.kafkastreams;

import szoeke.bence.kafkastreams.processor.Consumer;

public class KafkaStreamsApplication {

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.consumeRecords();
    }
}
