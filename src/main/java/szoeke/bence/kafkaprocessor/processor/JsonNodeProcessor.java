package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.streams.KeyValue;

import java.util.List;
import java.util.stream.StreamSupport;

import static szoeke.bence.kafkaprocessor.utility.JsonNodeCollector.toJsonNode;

public final class JsonNodeProcessor {

    private static final List<String> IGNORABLE_FIELD_NAMES = List.of("From", "To", "Via");
    private static final String EVENT_RECORD_HEADER = "eventRecordHeader";
    private static final String EVENT_INFO = "eventInfo";
    private static final String SIP_MESSAGES = "SipMessages";
    private static final String HEADER_FIELDS = "HeaderFields";

    private JsonNodeProcessor() {
    }

    public static String getEventRecordHeaderResultAsText(String key, JsonNode event) {
        return getEventRecordHeaderResultNode(event).asText();
    }

    //filter by result
    public static boolean filterEventRecordHeaderByResult(String key, JsonNode event) {
        return getEventRecordHeaderResultNode(event).intValue() == 1;
    }

    //remove KeyIds
    public static KeyValue<String, JsonNode> removeKeyIdsFromEventRecordHeader(String key, JsonNode event) {
        ObjectNode objectNodeEvent = (ObjectNode) event;
        ((ObjectNode) objectNodeEvent.get(EVENT_RECORD_HEADER)).remove("KeyIds");
        return KeyValue.pair(key, objectNodeEvent);
    }

    //remove headerfield list items where headerfield name equals to "From", "To" or "Via"
    public static KeyValue<String, JsonNode> mapHeaderFieldName(String key, JsonNode event) {
        ObjectNode objectNodeEvent = (ObjectNode) event;
        ArrayNode modifiedSipMessages = StreamSupport
                .stream(objectNodeEvent.get(EVENT_INFO).get(SIP_MESSAGES).spliterator(), true)
                .map(JsonNodeProcessor::mapSipMessage)
                .collect(toJsonNode());
        ((ObjectNode) objectNodeEvent.get(EVENT_INFO)).replace(SIP_MESSAGES, modifiedSipMessages);
        return KeyValue.pair(key, objectNodeEvent);
    }

    private static JsonNode mapSipMessage(JsonNode sipMessage) {
        ObjectNode objectNodeSipMessage = (ObjectNode) sipMessage;
        ArrayNode modifiedHeaderFields = StreamSupport
                .stream(objectNodeSipMessage.get(HEADER_FIELDS).spliterator(), true)
                .filter(headerField -> !IGNORABLE_FIELD_NAMES.contains(headerField.get("Name").asText()))
                .collect(toJsonNode());
        objectNodeSipMessage.replace(HEADER_FIELDS, modifiedHeaderFields);
        return objectNodeSipMessage;
    }

    private static JsonNode getEventRecordHeaderResultNode(JsonNode event) {
        return event.get(EVENT_RECORD_HEADER).get("Result");
    }
}