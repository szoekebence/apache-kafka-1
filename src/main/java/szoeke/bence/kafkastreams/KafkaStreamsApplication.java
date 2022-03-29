package szoeke.bence.kafkastreams;

import szoeke.bence.kafkastreams.processor.MyConsumer;

public class KafkaStreamsApplication {

    public static void main(String[] args) {
        MyConsumer consumer = new MyConsumer();
        consumer.consumeRecords();
    }
}
