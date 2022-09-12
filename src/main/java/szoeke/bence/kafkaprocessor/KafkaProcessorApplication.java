package szoeke.bence.kafkaprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkaprocessor.config.FilterConfig;
import szoeke.bence.kafkaprocessor.config.KafkaStreamsConfig;
import szoeke.bence.kafkaprocessor.processor.JsonNodeProcessor;
import szoeke.bence.kafkaprocessor.processor.StreamProcessor;

public class KafkaProcessorApplication {

    public static void main(String[] args) {
        KafkaStreamsConfig kafkaStreamsConfig = new KafkaStreamsConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        FilterConfig filterConfig = new FilterConfig(objectMapper);
        JsonNodeProcessor jsonNodeProcessor = new JsonNodeProcessor(filterConfig.generateConditions());
        StreamProcessor processor = new StreamProcessor(kafkaStreamsConfig.generateConfig(), jsonNodeProcessor, objectMapper);
        processor.processEvents();
    }
}
