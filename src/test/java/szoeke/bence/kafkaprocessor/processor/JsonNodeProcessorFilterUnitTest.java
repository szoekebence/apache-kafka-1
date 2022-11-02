package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import szoeke.bence.kafkaprocessor.entity.FilterData;
import szoeke.bence.kafkaprocessor.processor.mock.ConditionConfigFake;

import java.util.Set;

public class JsonNodeProcessorFilterUnitTest {

    private JsonNodeProcessor jsonNodeProcessor;
    private ConditionConfigFake conditionConfig;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        conditionConfig = new ConditionConfigFake();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void filterKeepTest() throws JsonProcessingException {
        conditionConfig.result = new FilterData()
                .setPath("/eventRecordHeader/KeyIds/ServedUser")
                .setValues(Set.of("NoMatchForThis", "012345678910"));
        jsonNodeProcessor = new JsonNodeProcessor(conditionConfig);

        Assert.assertTrue(jsonNodeProcessor.filter(generateJsonNodeToBeFiltered()));
    }

    @Test
    public void filterDropTest() throws JsonProcessingException {
        conditionConfig.result = new FilterData()
                .setPath("/eventRecordHeader/KeyIds/ServedUser")
                .setValues(Set.of("NoMatchForThis1", "NoMatchForThis2"));
        jsonNodeProcessor = new JsonNodeProcessor(conditionConfig);

        Assert.assertFalse(jsonNodeProcessor
                .filter(generateJsonNodeToBeFiltered()));
    }

    private JsonNode generateJsonNodeToBeFiltered() throws JsonProcessingException {
        return objectMapper.readTree("{" +
                "    \"eventRecordHeader\": {" +
                "        \"KeyIds\": {" +
                "            \"ServedUser\": \"<priv1>abc:012345678910@abc.abc012.acb123.567asdfgh.jkl</priv1>\"" +
                "        }" +
                "    }" +
                "}");
    }
}
