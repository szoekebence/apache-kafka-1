package szoeke.bence.kafkastreamprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkastreamprocessor.processor.StreamProcessor;

public class KafkaStreamProcessorApplication {

    public static void main(String[] args) {
        StreamProcessor processor = new StreamProcessor(new ObjectMapper());
        processor.processEvents();
    }
}
