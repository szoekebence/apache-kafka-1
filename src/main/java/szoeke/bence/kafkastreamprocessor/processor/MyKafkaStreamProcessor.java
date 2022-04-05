package szoeke.bence.kafkastreamprocessor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import szoeke.bence.kafkastreamprocessor.entity.Event;
import szoeke.bence.kafkastreamprocessor.utility.EventDeserializer;
import szoeke.bence.kafkastreamprocessor.utility.EventSerializer;

import java.time.Duration;
import java.util.Properties;

public class MyKafkaStreamProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaStreamProcessor.class);
    private static final String INPUT_TOPIC = "streams-input";
    private static final String OUTPUT_TOPIC = "streams-output";
    private final Properties properties;
    private final Serde<Event> eventSerde;
    private StreamsBuilder builder;

    public MyKafkaStreamProcessor(ObjectMapper objectMapper) {
        properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "MyStreamProcessorApp");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        eventSerde = Serdes.serdeFrom(new EventSerializer(objectMapper), new EventDeserializer(objectMapper));
        builder = new StreamsBuilder();
    }

    public void processRecords() {
        KStream<Long, Event> kStream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.Long(), eventSerde));
        kStream
                .filter(this::filterByResult)
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1)))
                .count()
                .toStream()
                .to(OUTPUT_TOPIC);
    }

    private boolean filterByResult(Long key, Event event) {
        boolean result = event.eventRecordHeader.Result == 1;
        LOGGER.info(String.format("Filter result: %s", result));
        return result;
    }
}
