package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import szoeke.bence.kafkaprocessor.utility.JsonNodeDeserializer;
import szoeke.bence.kafkaprocessor.utility.JsonNodeSerializer;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StreamProcessor {

    private static final String INPUT_TOPIC = "streams-input";
    private static final String OUTPUT_TOPIC = "streams-output";
    private final Properties properties;
    private final JsonNodeProcessor jsonNodeProcessor;
    private final Serde<String> stringSerde;
    private final Serde<JsonNode> jsonNodeSerde;
    private final StreamsBuilder builder;

    public StreamProcessor(Properties properties, JsonNodeProcessor jsonNodeProcessor, ObjectMapper objectMapper) {
        this.properties = properties;
        this.jsonNodeProcessor = jsonNodeProcessor;
        this.stringSerde = Serdes.String();
        this.jsonNodeSerde = Serdes.serdeFrom(
                new JsonNodeSerializer(objectMapper),
                new JsonNodeDeserializer(objectMapper));
        this.builder = new StreamsBuilder();
    }

    public void processEvents() {
        defineFilterOperations();
        startOperations();
    }

    private void defineFilterOperations() {
        builder
                .stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                .filter(jsonNodeProcessor::filter)
                .to(OUTPUT_TOPIC, Produced.with(stringSerde, jsonNodeSerde));
    }

    private void startOperations() {
        try (KafkaStreams streams = new KafkaStreams(builder.build(), properties)) {
            final CountDownLatch latch = new CountDownLatch(1);
            streams.start();
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
