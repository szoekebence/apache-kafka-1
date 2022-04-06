package szoeke.bence.kafkastreamprocessor.processor;

public class MyKafkaStreamProcessor {

//    private static final String INPUT_TOPIC = "streams-input";
//    private static final String OUTPUT_TOPIC = "streams-output";
//    private StreamsBuilder builder;
//
//    public void processRecords() {
//        KStream<Long, String> kStream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.Long(), Serdes.String()));
//        kStream
//                .filter(this::filterByResult)
//                .groupByKey()
//                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1)))
//                .count()
//                .toStream()
//                .to(OUTPUT_TOPIC, Produced.with(Serdes.Long(), Serdes.String()));
//    }
//
//    private boolean filterByResult(Long key, Event event) {
//        boolean result = event.eventRecordHeader.Result == 1;
//        return result;
//    }
}
