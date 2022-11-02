package szoeke.bence.kafkaprocessor.utility;

import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;

public class UnbiasedBlockAggregateSerde extends Serdes.WrapperSerde<HashMap<String, Long>> {

    public UnbiasedBlockAggregateSerde() {
        super(new UnbiasedBlockAggregateSerializer(), new UnbiasedBlockAggregateDeserializer());
    }
}
