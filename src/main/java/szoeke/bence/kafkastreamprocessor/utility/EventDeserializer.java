package szoeke.bence.kafkastreamprocessor.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import szoeke.bence.kafkastreamprocessor.entity.Event;

import java.io.IOException;

public class EventDeserializer implements Deserializer<Event> {

    private final ObjectMapper objectMapper;

    public EventDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Event deserialize(String str, byte[] data) {
        try {
            return hasNoData(data) ? null : objectMapper.readValue(data, Event.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private boolean hasNoData(byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }
}
