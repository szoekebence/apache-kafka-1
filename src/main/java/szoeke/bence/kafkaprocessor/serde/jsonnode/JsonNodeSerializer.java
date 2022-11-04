package szoeke.bence.kafkaprocessor.serde.jsonnode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class JsonNodeSerializer implements Serializer<JsonNode> {

    @Override
    public byte[] serialize(String str, JsonNode data) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, JsonNode data) {
        return serialize(null, data);
    }
}
