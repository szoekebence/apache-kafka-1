package szoeke.bence.kafkaprocessor.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.HashMap;

import static java.util.Objects.isNull;
import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class UnbiasedBlockAggregateDeserializer implements Deserializer<HashMap<String, Long>> {

    private final TypeReference<HashMap<String, Long>> stringHashMapTypeRef = new TypeReference<>() {
    };

    @Override
    public HashMap<String, Long> deserialize(String str, byte[] data) {
        try {
            return hasNoData(data) ? null : OBJECT_MAPPER.readValue(data, stringHashMapTypeRef);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public HashMap<String, Long> deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(null, data);
    }

    private boolean hasNoData(byte[] bytes) {
        return isNull(bytes) || bytes.length == 0;
    }
}
