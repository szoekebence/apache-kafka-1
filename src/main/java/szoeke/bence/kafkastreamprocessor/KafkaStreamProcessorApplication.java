package szoeke.bence.kafkastreamprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkastreamprocessor.processor.MyKafkaStreamProcessor;

public class KafkaStreamProcessorApplication {

    public static void main(String[] args) {
        MyKafkaStreamProcessor processor = new MyKafkaStreamProcessor(new ObjectMapper());
        while (true) {
            processor.processRecords();
        }
    }
}
