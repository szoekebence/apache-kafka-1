package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import szoeke.bence.kafkaprocessor.entity.FilterData;

public final class JsonNodeProcessor {

    private final FilterData filterData;

    public JsonNodeProcessor(FilterData filterData) {
        this.filterData = filterData;
    }

    boolean filter(Void unused, JsonNode jsonNode) {
        return filterData.values.stream().anyMatch(jsonNode.at(filterData.path).asText()::equals);
    }
}