package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import szoeke.bence.kafkaprocessor.entity.FilterData;

public final class JsonNodeProcessor {

    private final FilterData filterData;

    public JsonNodeProcessor(FilterData filterData) {
        this.filterData = filterData;
    }

    boolean filter(String unused, JsonNode jsonNode) {
        return filterData.values.contains(jsonNode.at(filterData.path).asText());
    }
}