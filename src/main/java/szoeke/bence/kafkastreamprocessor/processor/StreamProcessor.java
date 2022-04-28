package szoeke.bence.kafkastreamprocessor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import szoeke.bence.kafkastreamprocessor.entity.Event;
import szoeke.bence.kafkastreamprocessor.entity.innerentity.SipMessage;
import szoeke.bence.kafkastreamprocessor.utility.EventDeserializer;
import szoeke.bence.kafkastreamprocessor.utility.EventSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class StreamProcessor {

    private static final List<String> IGNORABLE_FIELD_NAMES = List.of("From", "To", "Via");
    private static final String INPUT_TOPIC = "streams-input";
    private static final String OUTPUT_TOPIC = "streams-output";
    private final Properties properties;
    private final Serde<Event> eventSerde;
    private final Serde<Long> longSerde;
    private final StreamsBuilder builder;

    public StreamProcessor(ObjectMapper objectMapper) {
        eventSerde = Serdes.serdeFrom(new EventSerializer(objectMapper), new EventDeserializer(objectMapper));
        this.longSerde = Serdes.Long();
        this.builder = new StreamsBuilder();
        this.properties = new Properties();
        this.properties.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "stream-processor");
        this.properties.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    }

    public void processEvents() {
        defineOperations();
        startOperationsWithShutdownHook();
    }

    private void defineOperations() {
        try (TimeWindowedSerializer<Long> serializer = new TimeWindowedSerializer<>(Serdes.Long().serializer());
             TimeWindowedDeserializer<Long> deserializer = new TimeWindowedDeserializer<>(Serdes.Long().deserializer())) {
            builder
                    .stream(INPUT_TOPIC, Consumed.with(longSerde, eventSerde))
                    .peek((key, event) -> System.out.printf("Actual key is %d.%n", key))
//                    .filter(this::filterEventRecordHeaderResult)
//                    .map(this::mapEventRecordHeaderKeyIds)
//                    .map(this::mapHeaderFieldName)
                    .groupByKey()
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(2500)))
                    .count()
                    .toStream()
                    .peek((key, value) -> System.out.printf("Output key and value is: %d | %d%n", key.key(), value))
                    .to(OUTPUT_TOPIC, Produced.with(Serdes.serdeFrom(serializer, deserializer), Serdes.Long()));
        }
    }

    //filter by result
    private boolean filterEventRecordHeaderResult(Long key, Event event) {
        return event.eventRecordHeader.Result == 1;
    }

    //remove KeyIds
    private KeyValue<Long, Event> mapEventRecordHeaderKeyIds(Long key, Event event) {
        event.eventRecordHeader.KeyIds = null;
        return KeyValue.pair(key, event);
    }

    //remove headerfield list items where headerfield name equals to "From", "To" or "Via"
    private KeyValue<Long, Event> mapHeaderFieldName(Long key, Event event) {
        event.eventInfo.SipMessages = event.eventInfo.SipMessages
                .parallelStream()
                .map(this::mapSipMessage)
                .collect(Collectors.toList());
        return KeyValue.pair(key, event);
    }

    private SipMessage mapSipMessage(SipMessage sipMessage) {
        sipMessage.HeaderFields = sipMessage.HeaderFields
                .parallelStream()
                .filter(headerField -> IGNORABLE_FIELD_NAMES.contains(headerField.Name))
                .collect(Collectors.toList());
        return sipMessage;
    }

    private void startOperationsWithShutdownHook() {
        try (KafkaStreams streams = new KafkaStreams(builder.build(), properties)) {
            final CountDownLatch latch = new CountDownLatch(1);
            try {
                streams.start();
                latch.await();
            } catch (final Throwable e) {
                System.exit(1);
            }
            Runtime.getRuntime().addShutdownHook(new Thread("stream-processor") {
                @Override
                public void run() {
                    latch.countDown();
                }
            });
        }
        System.exit(0);
    }
}
