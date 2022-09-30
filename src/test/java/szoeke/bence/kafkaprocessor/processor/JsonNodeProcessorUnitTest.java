package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import szoeke.bence.kafkaprocessor.entity.FilterData;

import java.util.Set;

public class JsonNodeProcessorUnitTest {

    @Test
    public void filterKeepTest() throws JsonProcessingException {
        final String testJson = "{" +
                "    \"eventRecordHeader\": {" +
                "        \"KeyIds\": {" +
                "            \"ServedUser\": \"<priv1>sip:0123456789@abc.abc012.acb123.567asdfgh.jkl</priv1>\"" +
                "        }" +
                "    }" +
                "}";
        FilterData filterData = new FilterData()
                .setPath("/eventRecordHeader/KeyIds/ServedUser")
                .setValues(Set.of("NoMatchForThis", "0123456789"));
        JsonNodeProcessor jsonNodeProcessor = new JsonNodeProcessor(filterData);
        Assert.assertTrue(jsonNodeProcessor.filter(null, new ObjectMapper().readTree(testJson)));
    }

    @Test
    public void filterDropTest() throws JsonProcessingException {
        final String testJson = "{" +
                "    \"eventRecordHeader\": {" +
                "        \"KeyIds\": {" +
                "            \"ServedUser\": \"<priv1>sip:0123456789@abc.abc012.acb123.567asdfgh.jkl</priv1>\"" +
                "        }" +
                "    }" +
                "}";
        FilterData filterData = new FilterData()
                .setPath("/eventRecordHeader/KeyIds/ServedUser")
                .setValues(Set.of("NoMatchForThis1", "NoMatchForThis2"));
        JsonNodeProcessor jsonNodeProcessor = new JsonNodeProcessor(filterData);
        Assert.assertFalse(jsonNodeProcessor.filter(null, new ObjectMapper().readTree(testJson)));
    }
}
