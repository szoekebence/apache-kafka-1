package szoeke.bence.kafkaprocessor.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;

import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class UnbiasedBlockAggregateSerializer implements Serializer<HashMap<String, Long>> {

    @Override
    public byte[] serialize(String str, HashMap<String, Long> data) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, HashMap<String, Long> data) {
        return serialize(null, data);
    }
}
