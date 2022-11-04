package szoeke.bence.kafkaprocessor.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import szoeke.bence.kafkaprocessor.entity.FilterData;
import szoeke.bence.kafkaprocessor.entity.OperationType;

import java.util.HashSet;
import java.util.Set;

import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;
import static szoeke.bence.kafkaprocessor.entity.OperationType.FILTER;

public class ConditionConfig {

    private static final String OPERATION_CONDITION_ENV_VAR = "OPERATION_CONDITION";
    private final OperationType operationType;
    private final String operationConditions;

    public ConditionConfig(OperationType operationType) {
        this.operationType = operationType;
        this.operationConditions = System.getenv(OPERATION_CONDITION_ENV_VAR);
    }

    public FilterData generateFilterConditions() {
        if (operationType == FILTER) {
            final String[] conditionParts = operationConditions.split(" in ");
            return new FilterData()
                    .setPath(conditionParts[0])
                    .setValues(generateValues(conditionParts));
        }
        return null;
    }

    private Set<String> generateValues(String[] conditionParts) {
        final TypeReference<HashSet<String>> stringHashSetTypeRef = new TypeReference<>() {
        };
        try {
            return OBJECT_MAPPER.readValue(conditionParts[1], stringHashSetTypeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Operation parsing exception.", e);
        }
    }
}
