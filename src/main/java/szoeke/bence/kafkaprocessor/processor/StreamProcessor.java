package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import szoeke.bence.kafkaprocessor.utility.JsonNodeDeserializer;
import szoeke.bence.kafkaprocessor.utility.JsonNodeSerializer;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StreamProcessor {

    private enum OPERATION_TYPE {
        STATELESS,
        STATEFUL
    }

    private static final String INPUT_TOPIC = "streams-input";
    private static final String OUTPUT_TOPIC = "streams-output";
    private static final String OPERATION_TYPE_ENV_VAR = "OPERATION_TYPE";
    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private static final String TIME_WINDOW_SIZE_MS_ENV_VAR = "TIME_WINDOW_SIZE_MS";
    private final Properties properties;
    private final Serde<JsonNode> jsonNodeSerde;
    private final Serde<String> stringSerde;
    private final StreamsBuilder builder;
    private final OPERATION_TYPE operationType;
    private final long timeWindowSize;

    public StreamProcessor(ObjectMapper objectMapper) {
        this.jsonNodeSerde = Serdes.serdeFrom(new JsonNodeSerializer(objectMapper), new JsonNodeDeserializer(objectMapper));
        this.stringSerde = Serdes.String();
        this.builder = new StreamsBuilder();
        this.properties = new Properties();
        this.properties.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "stream-processor");
        this.properties.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        this.operationType = OPERATION_TYPE.valueOf(System.getenv(OPERATION_TYPE_ENV_VAR));
        this.timeWindowSize = Long.parseLong(System.getenv(TIME_WINDOW_SIZE_MS_ENV_VAR));
    }

    public void processEvents() {
        chooseOperation();
        startOperations();
    }

    private void chooseOperation() {
        switch (operationType) {
            case STATELESS:
                defineFilterAndMapOperations();
                break;
            case STATEFUL:
                defineWindowedByAndCountOperations();
                break;
            default:
                throw new RuntimeException("Unexpected operation type.");
        }
    }

    private void defineFilterAndMapOperations() {
        builder
                .stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                .filter(JsonNodeProcessor::filterEventRecordHeaderByResult)
                .map(JsonNodeProcessor::removeKeyIdsFromEventRecordHeader)
                .map(JsonNodeProcessor::mapHeaderFieldName)
                .to(OUTPUT_TOPIC, Produced.with(stringSerde, jsonNodeSerde));
    }

    private void defineWindowedByAndCountOperations() {
        try (TimeWindowedSerializer<String> serializer = new TimeWindowedSerializer<>(stringSerde.serializer());
             TimeWindowedDeserializer<String> deserializer = new TimeWindowedDeserializer<>(stringSerde.deserializer())) {
            builder
                    .stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                    .groupBy(JsonNodeProcessor::getEventRecordHeaderResultAsText, Grouped.with(stringSerde, jsonNodeSerde))
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(timeWindowSize)))
                    .count()
                    .mapValues(Object::toString)
                    .toStream()
                    .to(OUTPUT_TOPIC, Produced.with(Serdes.serdeFrom(serializer, deserializer), stringSerde));
        }
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
