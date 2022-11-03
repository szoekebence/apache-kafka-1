package szoeke.bence.kafkaprocessor.serde.basicblockaggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import szoeke.bence.kafkaprocessor.entity.BasicBlockAggregate;

import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class BasicBlockAggregateSerializer implements Serializer<BasicBlockAggregate> {

    @Override
    public byte[] serialize(String str, BasicBlockAggregate data) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, BasicBlockAggregate data) {
        return serialize(null, data);
    }
}
