package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import szoeke.bence.kafkaprocessor.entity.BasicBlockAggregate;
import szoeke.bence.kafkaprocessor.processor.mock.ConditionConfigFake;

public class JsonNodeProcessorBasicBlockAggregationUnitTest {

    private static final String ERRORCODE_STARTS_WITH_4_JSON = "{\"eventRecordHeader\":{\"Result\":1,\"Cause\":{\"ErrorCode\":400}}}";
    private static final String ERRORCODE_STARTS_WITH_5_JSON = "{\"eventRecordHeader\":{\"Result\":1,\"Cause\":{\"ErrorCode\":500}}}";
    private static final String ERRORCODE_STARTS_WITH_6_JSON = "{\"eventRecordHeader\":{\"Result\":1,\"Cause\":{\"ErrorCode\":600}}}";
    private static final String SUCCESS_RESULT_JSON = "{\"eventRecordHeader\":{\"Result\":0}}";
    private JsonNodeProcessor jsonNodeProcessor;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonNodeProcessor = new JsonNodeProcessor(new ConditionConfigFake());
    }

    @Test
    public void successResultTest() throws JsonProcessingException {
        BasicBlockAggregate result = jsonNodeProcessor.doBasicBlockAggregation(objectMapper
                .readTree(SUCCESS_RESULT_JSON), new BasicBlockAggregate());
        Assert.assertEquals(0, result.failed_result);
        Assert.assertEquals(1, result.successful_result);
        Assert.assertEquals(0, result.err_starts_4);
        Assert.assertEquals(0, result.err_starts_5);
        Assert.assertEquals(0, result.err_starts_6);
        assertZeroValues(result);
    }

    @Test
    public void noErrorCodeStartsWith4Test() throws JsonProcessingException {
        BasicBlockAggregate result = jsonNodeProcessor.doBasicBlockAggregation(objectMapper
                .readTree(ERRORCODE_STARTS_WITH_4_JSON), new BasicBlockAggregate());
        Assert.assertEquals(1, result.failed_result);
        Assert.assertEquals(0, result.successful_result);
        Assert.assertEquals(1, result.err_starts_4);
        Assert.assertEquals(0, result.err_starts_5);
        Assert.assertEquals(0, result.err_starts_6);
        assertZeroValues(result);
    }

    @Test
    public void noErrorCodeStartsWith5Test() throws JsonProcessingException {
        BasicBlockAggregate result = jsonNodeProcessor.doBasicBlockAggregation(objectMapper
                .readTree(ERRORCODE_STARTS_WITH_5_JSON), new BasicBlockAggregate());
        Assert.assertEquals(1, result.failed_result);
        Assert.assertEquals(0, result.successful_result);
        Assert.assertEquals(0, result.err_starts_4);
        Assert.assertEquals(1, result.err_starts_5);
        Assert.assertEquals(0, result.err_starts_6);
        assertZeroValues(result);
    }

    @Test
    public void noErrorCodeStartsWith6Test() throws JsonProcessingException {
        BasicBlockAggregate result = jsonNodeProcessor.doBasicBlockAggregation(objectMapper
                .readTree(ERRORCODE_STARTS_WITH_6_JSON), new BasicBlockAggregate());
        Assert.assertEquals(1, result.failed_result);
        Assert.assertEquals(0, result.successful_result);
        Assert.assertEquals(0, result.err_starts_4);
        Assert.assertEquals(0, result.err_starts_5);
        Assert.assertEquals(1, result.err_starts_6);
        assertZeroValues(result);
    }

    @Test
    public void noErrorCodeStartsWith6Test2() throws JsonProcessingException {
        BasicBlockAggregate result = jsonNodeProcessor.doBasicBlockAggregation(objectMapper
                .readTree(ERRORCODE_STARTS_WITH_6_JSON), new BasicBlockAggregate()
                .setFailed_result(1)
                .setSuccessful_result(1)
                .setErr_starts_4(1)
                .setErr_starts_5(1)
                .setErr_starts_6(1)
                .setHas_sub_cause(1)
                .setProtocol_dns(1)
                .setProtocol_diameter(1)
                .setProtocol_diameter_err_starts_3(1)
                .setProtocol_diameter_err_starts_4(1)
                .setProtocol_diameter_err_starts_5(1)
        );
        Assert.assertEquals(2, result.failed_result);
        Assert.assertEquals(1, result.successful_result);
        Assert.assertEquals(1, result.err_starts_4);
        Assert.assertEquals(1, result.err_starts_5);
        Assert.assertEquals(2, result.err_starts_6);
        Assert.assertEquals(1, result.has_sub_cause);
        Assert.assertEquals(1, result.protocol_dns);
        Assert.assertEquals(1, result.protocol_diameter);
        Assert.assertEquals(1, result.protocol_diameter_err_starts_3);
        Assert.assertEquals(1, result.protocol_diameter_err_starts_4);
        Assert.assertEquals(1, result.protocol_diameter_err_starts_5);
    }

    private static void assertZeroValues(BasicBlockAggregate result) {
        Assert.assertEquals(0, result.has_sub_cause);
        Assert.assertEquals(0, result.protocol_dns);
        Assert.assertEquals(0, result.protocol_diameter);
        Assert.assertEquals(0, result.protocol_diameter_err_starts_3);
        Assert.assertEquals(0, result.protocol_diameter_err_starts_4);
        Assert.assertEquals(0, result.protocol_diameter_err_starts_5);
    }
}
