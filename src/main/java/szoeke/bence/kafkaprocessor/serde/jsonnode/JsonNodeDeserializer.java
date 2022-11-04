package szoeke.bence.kafkaprocessor.serde.jsonnode;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

import static java.util.Objects.isNull;
import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class JsonNodeDeserializer implements Deserializer<JsonNode> {

    @Override
    public JsonNode deserialize(String str, byte[] data) {
        try {
            return hasNoData(data) ? null : OBJECT_MAPPER.readTree(data);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public JsonNode deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(null, data);
    }

    private boolean hasNoData(byte[] bytes) {
        return isNull(bytes) || bytes.length == 0;
    }
}
