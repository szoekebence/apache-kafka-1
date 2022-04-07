package szoeke.bence.kafkastreamprocessor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import szoeke.bence.kafkastreamprocessor.entity.Event;
import szoeke.bence.kafkastreamprocessor.entity.innerentity.SipMessage;
import szoeke.bence.kafkastreamprocessor.utility.EventDeserializer;
import szoeke.bence.kafkastreamprocessor.utility.EventSerializer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StreamProcessor {

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
        startOperations();
    }

    private void startOperations() {
        final KafkaStreams streams = new KafkaStreams(builder.build(), properties);
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
                streams.close();
                latch.countDown();
            }
        });
        System.exit(0);
    }

    private void defineOperations() {
        builder
                .stream(INPUT_TOPIC, Consumed.with(longSerde, eventSerde))
                .mapValues(this::mapValues)
                .to(OUTPUT_TOPIC, Produced.with(longSerde, eventSerde));
    }

    //remove headerfield list items where headerfield name equals to "From", "To" or "Via"
    private Event mapValues(Long key, Event event) {
        List<String> ignorableFieldNames = List.of("From", "To", "Via");
        for (SipMessage message : event.eventInfo.SipMessages) {
            message.HeaderFields
                    .removeIf(headerField -> ignorableFieldNames.contains(headerField.Name));
        }
        return event;
    }

//remove KeyIds
//    private Event mapValues(Long object, Event event) {
//        event.eventRecordHeader.KeyIds = null;
//        return event;
//    }


//filter by result:
//    private void defineOperations() {
//        builder
//                .stream(INPUT_TOPIC, Consumed.with(longSerde, eventSerde))
//                .filter(this::filterResult)
//                .to(OUTPUT_TOPIC, Produced.with(longSerde, eventSerde));
//    }
//
//    private boolean filterResult(Long key, Event event) {
//        return event.eventRecordHeader.Result == 1;
//    }
}
