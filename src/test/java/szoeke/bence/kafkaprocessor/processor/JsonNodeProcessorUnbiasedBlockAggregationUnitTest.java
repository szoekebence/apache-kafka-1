//package szoeke.bence.kafkaprocessor.processor;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import szoeke.bence.kafkaprocessor.processor.mock.ConditionConfigFake;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class JsonNodeProcessorUnbiasedBlockAggregationUnitTest {
//
//    private static final String ERRORCODE_400_JSON = "{\"eventRecordHeader\":{\"Result\":1,\"Cause\":{\"ErrorCode\":400}}}";
//    private static final String ERRORCODE_500_JSON = "{\"eventRecordHeader\":{\"Result\":1,\"Cause\":{\"ErrorCode\":500}}}";
//    private JsonNodeProcessor jsonNodeProcessor;
//    private ObjectMapper objectMapper;
//
//    @Before
//    public void setUp() {
//        objectMapper = new ObjectMapper();
//        jsonNodeProcessor = new JsonNodeProcessor(new ConditionConfigFake());
//    }
//
//    @Test
//    public void oneErrorCodeTest() throws JsonProcessingException {
//        Set<String> result = jsonNodeProcessor.doUnbiasedBlockAggregation(objectMapper
//                .readTree(ERRORCODE_400_JSON), new HashSet<>());
//        Assert.assertNotNull(result);
//        Assert.assertEquals(1, result.size());
//        Assert.assertTrue(result.contains("400"));
//    }
//
//    @Test
//    public void twoSameErrorCodeTest() throws JsonProcessingException {
//        HashSet<String> aggregate = new HashSet<>();
//        aggregate.add("400");
//        Set<String> result = jsonNodeProcessor.doUnbiasedBlockAggregation(objectMapper
//                .readTree(ERRORCODE_400_JSON), aggregate);
//        Assert.assertNotNull(result);
//        Assert.assertEquals(1, result.size());
//        Assert.assertTrue(result.contains("400"));
//    }
//
//    @Test
//    public void twoOtherErrorCodeTest() throws JsonProcessingException {
//        HashSet<String> aggregate = new HashSet<>();
//        aggregate.add("400");
//        Set<String> result = jsonNodeProcessor.doUnbiasedBlockAggregation(objectMapper
//                .readTree(ERRORCODE_500_JSON), aggregate);
//        Assert.assertNotNull(result);
//        Assert.assertEquals(2, result.size());
//        Assert.assertTrue(result.contains("400"));
//        Assert.assertTrue(result.contains("500"));
//    }
//}
