package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import szoeke.bence.kafkaprocessor.entity.AverageBlockAggregate;
import szoeke.bence.kafkaprocessor.processor.mock.ConditionConfigFake;

public class EventProcessorAverageBlockAggregationUnitTest {

    private static final String JSON_5_DURATION = "{\"eventRecordHeader\":{\"StartTime\":5,\"EndTime\":10}}";
    private EventProcessor jsonNodeProcessor;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonNodeProcessor = new EventProcessor(new ConditionConfigFake());
    }

    @Test
    public void oneEventTest() throws JsonProcessingException {
        AverageBlockAggregate result = jsonNodeProcessor.doAverageBlockAggregation(objectMapper
                .readTree(JSON_5_DURATION), new AverageBlockAggregate());
        Assert.assertNotNull(result);
        Assert.assertEquals(1.0f, result.numberOfEvents, 0.0f);
        Assert.assertEquals(5.0f, result.sumOfDurations, 0.0f);
    }

    @Test
    public void moreEventTest() throws JsonProcessingException {
        AverageBlockAggregate result = jsonNodeProcessor.doAverageBlockAggregation(objectMapper
                .readTree(JSON_5_DURATION), new AverageBlockAggregate()
                .setNumberOfEvents(3.0f)
                .setSumOfDurations(16.0f));
        Assert.assertNotNull(result);
        Assert.assertEquals(4.0f, result.numberOfEvents, 0.0f);
        Assert.assertEquals(21.0f, result.sumOfDurations, 0.0f);
    }

    @Test
    public void calcAverageTest() {
        Float result = jsonNodeProcessor.calcAverageBlockAggregation(
                new AverageBlockAggregate()
                        .setNumberOfEvents(4.0f)
                        .setSumOfDurations(21.0f));
        Assert.assertNotNull(result);
        Assert.assertEquals(5.25f, result, 0.0f);
    }
}
