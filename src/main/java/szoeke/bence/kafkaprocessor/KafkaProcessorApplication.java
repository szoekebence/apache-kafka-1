package szoeke.bence.kafkaprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkaprocessor.config.ConditionConfig;
import szoeke.bence.kafkaprocessor.config.KafkaStreamsConfig;
import szoeke.bence.kafkaprocessor.entity.OperationType;
import szoeke.bence.kafkaprocessor.processor.JsonNodeProcessor;
import szoeke.bence.kafkaprocessor.processor.StreamProcessor;

public class KafkaProcessorApplication {

    private static final String OPERATION_TYPE_ENV_VAR = "OPERATION_TYPE";

    public static void main(String[] args) {
        OperationType operationType = OperationType.valueOf(System.getenv(OPERATION_TYPE_ENV_VAR));
        ObjectMapper objectMapper = new ObjectMapper();
        new StreamProcessor(
                new KafkaStreamsConfig().generateConfig(),
                new JsonNodeProcessor(new ConditionConfig(objectMapper, operationType)),
                objectMapper,
                operationType
        ).processEvents();
    }
}
