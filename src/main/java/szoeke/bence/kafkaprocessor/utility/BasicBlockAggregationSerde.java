package szoeke.bence.kafkaprocessor.utility;

import org.apache.kafka.common.serialization.Serdes;
import szoeke.bence.kafkaprocessor.entity.BasicBlockAggregate;

public class BasicBlockAggregationSerde extends Serdes.WrapperSerde<BasicBlockAggregate> {

    public BasicBlockAggregationSerde() {
        super(new BasicBlockAggregateSerializer(), new BasicBlockAggregateDeserializer());
    }
}
