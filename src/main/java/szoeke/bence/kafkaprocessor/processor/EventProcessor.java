package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import szoeke.bence.kafkaprocessor.config.ConditionConfig;
import szoeke.bence.kafkaprocessor.entity.AverageBlockAggregate;
import szoeke.bence.kafkaprocessor.entity.BasicBlockAggregate;
import szoeke.bence.kafkaprocessor.entity.FilterData;

import java.util.HashMap;

import static java.util.Objects.nonNull;

public final class EventProcessor {

    private final FilterData filterData;

    public EventProcessor(ConditionConfig conditionConfig) {
        this.filterData = conditionConfig.generateFilterConditions();
    }

    boolean filter(JsonNode jsonNode) {
        return filterData.values.stream().anyMatch(jsonNode.at(filterData.path).asText()::contains);
    }

    String anonymization(JsonNode jsonNode) {
        String servedUser = jsonNode
                .get("eventRecordHeader")
                .get("KeyIds")
                .get("ServedUser")
                .asText();
        String sensitiveData = servedUser.substring(11, 23);
        return jsonNode.toString().replaceAll(sensitiveData, "xxxxxx");
    }

    String getEventId(JsonNode jsonNode) {
        return jsonNode.get("eventRecordHeader").get("EventId").asText();
    }

    BasicBlockAggregate doBasicBlockAggregation(JsonNode jsonNode, BasicBlockAggregate aggregate) {
        JsonNode eventRecordHeader = jsonNode.get("eventRecordHeader");
        if ("1".equals(eventRecordHeader.get("Result").asText())) {
            aggregate.failed_result++;
            aggregateCause(eventRecordHeader.get("Cause"), aggregate);
        } else {
            aggregate.successful_result++;
        }
        return aggregate;
    }

    HashMap<Long, Long> doUnbiasedBlockAggregation(JsonNode jsonNode, HashMap<Long, Long> aggregate) {
        JsonNode cause = jsonNode.get("eventRecordHeader").get("Cause");
        if (nonNull(cause)) {
            JsonNode errorCode = cause.get("ErrorCode");
            if (nonNull(errorCode)) {
                aggregateErrorCode(aggregate, errorCode.asLong());
            }
        }
        return aggregate;
    }

    AverageBlockAggregate doAverageBlockAggregation(JsonNode jsonNode, AverageBlockAggregate aggregate) {
        long endTime = jsonNode.get("eventRecordHeader").get("EndTime").asLong();
        long startTime = jsonNode.get("eventRecordHeader").get("StartTime").asLong();
        aggregate.numberOfEvents++;
        aggregate.sumOfDurations += endTime - startTime;
        return aggregate;
    }

    Float calcAverageBlockAggregation(AverageBlockAggregate aggregate) {
        return aggregate.sumOfDurations / aggregate.numberOfEvents;
    }

    private void aggregateCause(JsonNode cause, BasicBlockAggregate aggregate) {
        aggregateErrorCode(cause.get("ErrorCode").asText(), aggregate);
        aggregateSubCause(cause.get("SubCause"), aggregate);
    }

    private void aggregateErrorCode(String errorCode, BasicBlockAggregate aggregate) {
        if (errorCode.startsWith("4")) {
            aggregate.err_starts_4++;
        } else if (errorCode.startsWith("5")) {
            aggregate.err_starts_5++;
        } else if (errorCode.startsWith("6")) {
            aggregate.err_starts_6++;
        }
    }

    private void aggregateSubCause(JsonNode subCause, BasicBlockAggregate aggregate) {
        if (nonNull(subCause)) {
            aggregate.has_sub_cause++;
            aggregateSubCauseParams(subCause, aggregate);
        }
    }

    private void aggregateSubCauseParams(JsonNode subCause, BasicBlockAggregate aggregate) {
        String subProtocolText = subCause.get("SubProtocol").asText();
        if ("DNS".equals(subProtocolText)) {
            aggregate.protocol_dns++;
        } else if ("Diameter".equals(subProtocolText)) {
            aggregate.protocol_diameter++;
            aggregateSubError(subCause.get("SubError").asText(), aggregate);
        }
    }

    private void aggregateSubError(String subErrorText, BasicBlockAggregate aggregate) {
        if (subErrorText.startsWith("3")) {
            aggregate.protocol_diameter_err_starts_3++;
        } else if (subErrorText.startsWith("4")) {
            aggregate.protocol_diameter_err_starts_4++;
        } else if (subErrorText.startsWith("5")) {
            aggregate.protocol_diameter_err_starts_5++;
        }
    }

    private void aggregateErrorCode(HashMap<Long, Long> aggregate, Long errorCode) {
        if (nonNull(aggregate.get(errorCode))) {
            aggregate.replace(errorCode, aggregate.get(errorCode) + 1L);
        } else {
            aggregate.put(errorCode, 1L);
        }
    }
}