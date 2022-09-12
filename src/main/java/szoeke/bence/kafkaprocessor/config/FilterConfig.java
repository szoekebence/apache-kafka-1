package szoeke.bence.kafkaprocessor.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import szoeke.bence.kafkaprocessor.entity.FilterData;

import java.util.HashSet;
import java.util.Set;

public class FilterConfig {

    private static final String OPERATION_CONDITION_ENV_VAR = "OPERATION_CONDITION";
    private final ObjectMapper objectMapper;

    public FilterConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public FilterData generateConditions() {
        final String[] conditionParts = System.getenv(OPERATION_CONDITION_ENV_VAR).split(" in ");
        return new FilterData()
                .setPath(conditionParts[0])
                .setValues(generateValues(conditionParts));
    }

    private Set<String> generateValues(String[] conditionParts) {
        final TypeReference<HashSet<String>> stringHashSetTypeRef = new TypeReference<>() {
        };
        try {
            return objectMapper.readValue(conditionParts[1], stringHashSetTypeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Operation parsing exception.", e);
        }
    }
}
