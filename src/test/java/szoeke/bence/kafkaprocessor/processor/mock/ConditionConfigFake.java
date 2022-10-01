package szoeke.bence.kafkaprocessor.processor.mock;

import szoeke.bence.kafkaprocessor.config.ConditionConfig;
import szoeke.bence.kafkaprocessor.entity.FilterData;

public class ConditionConfigFake extends ConditionConfig {

    public FilterData result;

    public ConditionConfigFake() {
        super(null, null);
    }

    @Override
    public FilterData generateFilterConditions() {
        return result;
    }
}
