package szoeke.bence.kafkaprocessor.utility;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import szoeke.bence.kafkaprocessor.entity.BasicBlockAggregate;

import java.io.IOException;

import static java.util.Objects.isNull;
import static szoeke.bence.kafkaprocessor.KafkaProcessorApplication.OBJECT_MAPPER;

public class BasicBlockAggregateDeserializer implements Deserializer<BasicBlockAggregate> {

    @Override
    public BasicBlockAggregate deserialize(String str, byte[] data) {
        try {
            return hasNoData(data) ? null : OBJECT_MAPPER.readValue(data, BasicBlockAggregate.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public BasicBlockAggregate deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(null, data);
    }

    private boolean hasNoData(byte[] bytes) {
        return isNull(bytes) || bytes.length == 0;
    }
}
