package szoeke.bence.kafkaprocessor.serde.averageblockaggregtion;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import szoeke.bence.kafkaprocessor.entity.AverageBlockAggregate;

import java.io.IOException;

import static java.util.Objects.isNull;
import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class AverageBlockAggregateDeserializer implements Deserializer<AverageBlockAggregate> {

    @Override
    public AverageBlockAggregate deserialize(String str, byte[] data) {
        try {
            return hasNoData(data) ? null : OBJECT_MAPPER.readValue(data, AverageBlockAggregate.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public AverageBlockAggregate deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(null, data);
    }

    private boolean hasNoData(byte[] bytes) {
        return isNull(bytes) || bytes.length == 0;
    }
}
