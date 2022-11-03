package szoeke.bence.kafkaprocessor.serde.averageblockaggregtion;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import szoeke.bence.kafkaprocessor.entity.AverageBlockAggregate;

import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class AverageBlockAggregateSerializer implements Serializer<AverageBlockAggregate> {

    @Override
    public byte[] serialize(String str, AverageBlockAggregate data) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, AverageBlockAggregate data) {
        return serialize(null, data);
    }
}
