package szoeke.bence.kafkaprocessor.utility;

import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;

public class UnbiasedBlockAggregationSerde extends Serdes.WrapperSerde<HashMap<String, Long>> {

    public UnbiasedBlockAggregationSerde() {
        super(new UnbiasedBlockAggregateSerializer(), new UnbiasedBlockAggregateDeserializer());
    }
}
