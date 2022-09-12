package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import szoeke.bence.kafkaprocessor.entity.FilterData;

import java.util.Set;

public class JsonNodeProcessorUnitTest {

    @Test
    public void filterTest() throws JsonProcessingException {
        final String testJson = "{" +
                "  \"jsonNode0\": [" +
                "    {" +
                "      \"jsonNode0\": \"jsonNode0\"" +
                "    }," +
                "    {" +
                "      \"jsonNode1\": \"jsonNode1\"" +
                "    }" +
                "  ]," +
                "  \"jsonNode1\": {" +
                "    \"jsonNode0\": {" +
                "      \"jsonNode3\": \"jsonNode3\"" +
                "    }," +
                "    \"jsonNode1\": {" +
                "      \"jsonNode3\": \"jsonNode3\"" +
                "    }," +
                "    \"jsonNode2\": {" +
                "      \"jsonNode3\": \"jsonNode3\"," +
                "      \"jsonNode4\": \"jsonNode4\"," +
                "      \"jsonNode5\": \"2\"," +
                "      \"jsonNode6\": \"jsonNode6\"" +
                "    }" +
                "  }," +
                "  \"jsonNode3\": [" +
                "    {" +
                "      \"jsonNode0\": \"jsonNode0\"" +
                "    }," +
                "    {" +
                "      \"jsonNode1\": \"jsonNode1\"" +
                "    }" +
                "  ]" +
                "}";
        FilterData filterData = new FilterData()
                .setPath("/jsonNode1/jsonNode2/jsonNode5")
                .setValues(Set.of("1", "2"));
        JsonNodeProcessor jsonNodeProcessor = new JsonNodeProcessor(filterData);
        Assert.assertTrue(jsonNodeProcessor.filter(null, new ObjectMapper().readTree(testJson)));
    }
}
