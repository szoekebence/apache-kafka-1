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

    private enum OPERATION_TYPE {
        FILTER_AND_MAP,
        FILTER_AND_WINDOWED_BY_AND_COUNT
    }

    private static final List<String> IGNORABLE_FIELD_NAMES = List.of("From", "To", "Via");
    private static final String INPUT_TOPIC = "streams-input";
    private static final String OUTPUT_TOPIC = "streams-output";
    private static final String OPERATION_TYPE_ENV_VAR = "OPERATION_TYPE";
    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private final Properties properties;
    private final Serde<Event> eventSerde;
    private final Serde<Long> longSerde;
    private final StreamsBuilder builder;
    private final OPERATION_TYPE operationType;

    public StreamProcessor(ObjectMapper objectMapper) {
        this.eventSerde = Serdes.serdeFrom(new EventSerializer(objectMapper), new EventDeserializer(objectMapper));
        this.longSerde = Serdes.Long();
        this.builder = new StreamsBuilder();
        this.properties = new Properties();
        this.properties.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "stream-processor");
        this.properties.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        this.operationType = OPERATION_TYPE.valueOf(System.getenv(OPERATION_TYPE_ENV_VAR));
    }

    public void processEvents() {
        chooseOperation();
        startOperations();
    }

    private void chooseOperation() {
        switch (operationType) {
            case FILTER_AND_MAP:
                defineFilterAndMapOperations();
                break;
            case FILTER_AND_WINDOWED_BY_AND_COUNT:
                defineFilterAndWindowedByAndCountOperations();
                break;
        }
    }

    private void defineFilterAndMapOperations() {
        builder
                .stream(INPUT_TOPIC, Consumed.with(longSerde, eventSerde))
                .filter(this::filterEventRecordHeaderResult)
                .map(this::mapEventRecordHeaderKeyIds)
                .map(this::mapHeaderFieldName)
                .to(OUTPUT_TOPIC, Produced.with(longSerde, eventSerde));
    }

    private void defineFilterAndWindowedByAndCountOperations() {
        try (TimeWindowedSerializer<Long> serializer = new TimeWindowedSerializer<>(Serdes.Long().serializer());
             TimeWindowedDeserializer<Long> deserializer = new TimeWindowedDeserializer<>(Serdes.Long().deserializer())) {
            builder
                    .stream(INPUT_TOPIC, Consumed.with(longSerde, eventSerde))
                    .filter(this::filterEventRecordHeaderResult)
                    .groupBy((key, event) -> event.eventRecordHeader.Cause.ErrorCode)
                    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(2500)))
                    .count()
                    .toStream()
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

    private void startOperations() {
        try (KafkaStreams streams = new KafkaStreams(builder.build(), properties)) {
            final CountDownLatch latch = new CountDownLatch(1);
            try {
                streams.start();
                latch.await();
            } catch (final Throwable e) {
                System.exit(1);
            }
        }
        System.exit(0);
    }
}
