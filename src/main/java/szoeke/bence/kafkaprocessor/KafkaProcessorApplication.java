package szoeke.bence.kafkaprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkaprocessor.processor.StreamProcessor;

public class KafkaProcessorApplication {

    public static void main(String[] args) {
        StreamProcessor processor = new StreamProcessor(new ObjectMapper());
        processor.processEvents();
    }
}
