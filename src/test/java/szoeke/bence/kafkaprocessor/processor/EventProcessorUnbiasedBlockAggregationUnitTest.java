package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import szoeke.bence.kafkaprocessor.processor.mock.ConditionConfigFake;

import java.util.HashMap;
import java.util.Map;

public class EventProcessorUnbiasedBlockAggregationUnitTest {

    private static final String ERRORCODE_400_JSON = "{\"eventRecordHeader\":{\"Result\":1,\"Cause\":{\"ErrorCode\":400}}}";
    private static final String ERRORCODE_500_JSON = "{\"eventRecordHeader\":{\"Result\":1,\"Cause\":{\"ErrorCode\":500}}}";
    private EventProcessor jsonNodeProcessor;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonNodeProcessor = new EventProcessor(new ConditionConfigFake());
    }

    @Test
    public void oneErrorCodeTest() throws JsonProcessingException {
        Map<Long, Long> result = jsonNodeProcessor.doUnbiasedBlockAggregation(objectMapper
                .readTree(ERRORCODE_400_JSON), new HashMap<>());
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey(400L));
        Assert.assertEquals(1L, result.get(400L).longValue());
    }

    @Test
    public void twoSameErrorCodeTest() throws JsonProcessingException {
        HashMap<Long, Long> aggregate = new HashMap<>();
        aggregate.put(400L, 1L);
        Map<Long, Long> result = jsonNodeProcessor.doUnbiasedBlockAggregation(objectMapper
                .readTree(ERRORCODE_400_JSON), aggregate);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey(400L));
        Assert.assertEquals(2L, result.get(400L).longValue());
    }

    @Test
    public void twoOtherErrorCodeTest() throws JsonProcessingException {
        HashMap<Long, Long> aggregate = new HashMap<>();
        aggregate.put(400L, 1L);
        Map<Long, Long> result = jsonNodeProcessor.doUnbiasedBlockAggregation(objectMapper
                .readTree(ERRORCODE_500_JSON), aggregate);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.containsKey(400L));
        Assert.assertTrue(result.containsKey(500L));
        Assert.assertEquals(1L, result.get(400L).longValue());
        Assert.assertEquals(1L, result.get(500L).longValue());
    }
}
