package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import szoeke.bence.kafkaprocessor.entity.AverageBlockAggregate;
import szoeke.bence.kafkaprocessor.entity.BasicBlockAggregate;
import szoeke.bence.kafkaprocessor.entity.OperationType;
import szoeke.bence.kafkaprocessor.serde.averageblockaggregtion.AverageBlockAggregateSerde;
import szoeke.bence.kafkaprocessor.serde.basicblockaggregation.BasicBlockAggregateSerde;
import szoeke.bence.kafkaprocessor.serde.jsonnode.JsonNodeDeserializer;
import szoeke.bence.kafkaprocessor.serde.jsonnode.JsonNodeSerializer;
import szoeke.bence.kafkaprocessor.serde.unbiasedblockaggregation.UnbiasedBlockAggregateSerde;

import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StreamProcessor {

    private static final String INPUT_TOPIC = "streams-input";
    private static final String OUTPUT_TOPIC = "streams-output";
    private final Properties properties;
    private final EventProcessor eventProcessor;
    private final OperationType operationType;
    private final Serde<String> stringSerde;
    private final Serde<JsonNode> jsonNodeSerde;
    private final StreamsBuilder builder;
    private final Serde<BasicBlockAggregate> basicBlockAggregateSerde;
    private final Serde<HashMap<Long, Long>> unbiasedBlockAggregateSerde;
    private final Serde<AverageBlockAggregate> averageBlockAggregateSerde;

    public StreamProcessor(Properties properties, EventProcessor eventProcessor,
                           OperationType operationType) {
        this.properties = properties;
        this.eventProcessor = eventProcessor;
        this.operationType = operationType;
        this.stringSerde = Serdes.String();
        this.jsonNodeSerde = Serdes.serdeFrom(new JsonNodeSerializer(), new JsonNodeDeserializer());
        this.basicBlockAggregateSerde = new BasicBlockAggregateSerde();
        this.unbiasedBlockAggregateSerde = new UnbiasedBlockAggregateSerde();
        this.averageBlockAggregateSerde = new AverageBlockAggregateSerde();
        this.builder = new StreamsBuilder();
    }

    public void processEvents() {
        chooseOperation();
        startOperations();
    }

    private void chooseOperation() {
        switch (operationType) {
            case FILTER:
                defineFilterOperations();
                break;
            case ANONYMIZATION:
                defineAnonymizationOperations();
                break;
            case BASIC_BLOCK_AGGREGATION:
                defineBasicBlockAggregationOperations();
                break;
            case UNBIASED_BLOCK_AGGREGATION:
                defineUnbiasedBlockAggregationOperations();
                break;
            case AVERAGING_BLOCK_AGGREGATION:
                defineAveragingBlockAggregationOperations();
                break;
        }
    }

    private void defineFilterOperations() {
        builder
                .stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                .filter((key, value) -> eventProcessor.filter(value))
                .to(OUTPUT_TOPIC, Produced.with(stringSerde, jsonNodeSerde));
    }

    private void defineAnonymizationOperations() {
        builder
                .stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                .map((key, value) -> new KeyValue<>(key, eventProcessor.anonymization(value)))
                .to(OUTPUT_TOPIC, Produced.with(stringSerde, stringSerde));
    }

    private void defineBasicBlockAggregationOperations() {
        builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                .groupBy((k, v) -> eventProcessor.getEventId(v))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1L)))
                .aggregate(
                        BasicBlockAggregate::new,
                        (k, v, aggV) -> eventProcessor.doBasicBlockAggregation(v, aggV),
                        Materialized.with(stringSerde, basicBlockAggregateSerde))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .to(OUTPUT_TOPIC);
    }

    private void defineUnbiasedBlockAggregationOperations() {
        builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                .groupBy((k, v) -> eventProcessor.getEventId(v))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1L)))
                .aggregate(
                        HashMap::new,
                        (k, v, aggV) -> eventProcessor.doUnbiasedBlockAggregation(v, aggV),
                        Materialized.with(stringSerde, unbiasedBlockAggregateSerde))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .to(OUTPUT_TOPIC);
    }

    private void defineAveragingBlockAggregationOperations() {
        builder.stream(INPUT_TOPIC, Consumed.with(stringSerde, jsonNodeSerde))
                .groupBy((k, v) -> eventProcessor.getEventId(v))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1L)))
                .aggregate(
                        AverageBlockAggregate::new,
                        (k, v, aggV) -> eventProcessor.doAverageBlockAggregation(v, aggV),
                        Materialized.with(stringSerde, averageBlockAggregateSerde))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .mapValues(eventProcessor::calcAverageBlockAggregation)
                .toStream()
                .to(OUTPUT_TOPIC);
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
