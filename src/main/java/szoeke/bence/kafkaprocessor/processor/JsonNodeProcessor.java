package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import szoeke.bence.kafkaprocessor.config.ConditionConfig;
import szoeke.bence.kafkaprocessor.entity.FilterData;

public final class JsonNodeProcessor {

    private final FilterData filterData;

    public JsonNodeProcessor(ConditionConfig conditionConfig) {
        this.filterData = conditionConfig.generateFilterConditions();
    }

    boolean filter(JsonNode jsonNode) {
        return filterData.values.stream().anyMatch(jsonNode.at(filterData.path).asText()::contains);
    }

    public String anonymization(JsonNode jsonNode) {
        String servedUser = jsonNode
                .get("eventRecordHeader")
                .get("KeyIds")
                .get("ServedUser")
                .asText();
        String sensitiveData = servedUser.substring(11, 23);
        return jsonNode.toString().replaceAll(sensitiveData, "xxxxxx");
    }
}