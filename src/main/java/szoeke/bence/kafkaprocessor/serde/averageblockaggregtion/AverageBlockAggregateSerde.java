package szoeke.bence.kafkaprocessor.serde.averageblockaggregtion;

import org.apache.kafka.common.serialization.Serdes;
import szoeke.bence.kafkaprocessor.entity.AverageBlockAggregate;

public class AverageBlockAggregateSerde extends Serdes.WrapperSerde<AverageBlockAggregate> {

    public AverageBlockAggregateSerde() {
        super(new AverageBlockAggregateSerializer(), new AverageBlockAggregateDeserializer());
    }
}
