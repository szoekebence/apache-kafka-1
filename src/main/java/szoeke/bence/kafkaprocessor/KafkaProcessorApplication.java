package szoeke.bence.kafkaprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkaprocessor.config.ConditionConfig;
import szoeke.bence.kafkaprocessor.config.KafkaStreamsConfig;
import szoeke.bence.kafkaprocessor.entity.OperationType;
import szoeke.bence.kafkaprocessor.processor.JsonNodeProcessor;
import szoeke.bence.kafkaprocessor.processor.StreamProcessor;

public class KafkaProcessorApplication {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String OPERATION_TYPE_ENV_VAR = "OPERATION_TYPE";

    public static void main(String[] args) {
        OperationType operationType = OperationType.valueOf(System.getenv(OPERATION_TYPE_ENV_VAR));
        new StreamProcessor(
                new KafkaStreamsConfig(operationType).generateConfig(),
                new JsonNodeProcessor(new ConditionConfig(operationType)),
                operationType
        ).processEvents();
    }
}
