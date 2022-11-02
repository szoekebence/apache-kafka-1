package szoeke.bence.kafkaprocessor.utility;

import org.apache.kafka.common.serialization.Serdes;
import szoeke.bence.kafkaprocessor.entity.BasicBlockAggregate;

public class BasicBlockAggregateSerde extends Serdes.WrapperSerde<BasicBlockAggregate> {

    public BasicBlockAggregateSerde() {
        super(new BasicBlockAggregateSerializer(), new BasicBlockAggregateDeserializer());
    }
}
