package szoeke.bence.kafkaprocessor.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import szoeke.bence.kafkaprocessor.processor.mock.ConditionConfigFake;

public class EventProcessorAnonymizationUnitTest {

    private ObjectMapper objectMapper;

    @Test
    public void anonymizationTest() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        EventProcessor jsonNodeProcessor = new EventProcessor(new ConditionConfigFake());
        Assert.assertEquals(
                generateAnonymizationResult(),
                jsonNodeProcessor.anonymization(generateJsonNodeToAnonymization()));
    }

    private JsonNode generateJsonNodeToAnonymization() throws JsonProcessingException {
        return objectMapper.readTree("{" +
                "  \"eventRecordHeader\": {" +
                "    \"KeyIds\": {" +
                "      \"ServedUser\": \"<priv1>abc:012345678910@ims.mnc006.mcc240.3gppnetwork.org</priv1>\"," +
                "      \"Impi\": \"<priv30>012345678910@abc.def012.def234.678example.com</priv30>\"" +
                "    }" +
                "  }," +
                "  \"eventInfo\": {" +
                "    \"SipMessages\": [" +
                "      {" +
                "        \"Time\": 123," +
                "        \"Direction\": \"ASD\"," +
                "        \"StartLine\": {" +
                "          \"Request\": \"<priv1>FGHJK abc:+012345678910 EFG/123</priv1>\"" +
                "        }," +
                "        \"HeaderFields\": [" +
                "          {" +
                "            \"Name\": \"CSeq\"," +
                "            \"Values\": [" +
                "              \"1 INVITE\"" +
                "            ]" +
                "          }," +
                "          {" +
                "            \"Name\": \"Contact\"," +
                "            \"Values\": [" +
                "              \"<priv1>jkl:012345678910@01.23.45.67;+abcdefghj=\\\"<urn:gsma:imei:46860000-042004-0>\\\";asdfghjlk012345678910afgfhhj\"" +
                "            ]" +
                "          }" +
                "        ]" +
                "      }" +
                "    ]" +
                "  }," +
                "  \"DiameterMessages\": [" +
                "    {" +
                "      \"Time\": 123," +
                "      \"SessionId\": \"<priv1>ABCD.a01.bcd.ef;01234;01234;01234;01;0;abc:012345678910@abc.def012.ghj012.abcd.asd</priv1>\"" +
                "    }" +
                "  ]" +
                "}");
    }

    private String generateAnonymizationResult() {
        return "{\"eventRecordHeader\":{\"KeyIds\":{\"ServedUser\":\"<priv1>abc:xxxxxx@ims.mnc006." +
                "mcc240.3gppnetwork.org</priv1>\",\"Impi\":\"<priv30>xxxxxx@abc.def012.def234.678e" +
                "xample.com</priv30>\"}},\"eventInfo\":{\"SipMessages\":[{\"Time\":123,\"Direction" +
                "\":\"ASD\",\"StartLine\":{\"Request\":\"<priv1>FGHJK abc:+xxxxxx EFG/123</priv1>\"" +
                "},\"HeaderFields\":[{\"Name\":\"CSeq\",\"Values\":[\"1 INVITE\"]},{\"Name\":\"Con" +
                "tact\",\"Values\":[\"<priv1>jkl:xxxxxx@01.23.45.67;+abcdefghj=\\\"<urn:gsma:imei:" +
                "46860000-042004-0>\\\";asdfghjlkxxxxxxafgfhhj\"]}]}]},\"DiameterMessages\":[{\"Ti" +
                "me\":123,\"SessionId\":\"<priv1>ABCD.a01.bcd.ef;01234;01234;01234;01;0;abc:xxxxxx" +
                "@abc.def012.ghj012.abcd.asd</priv1>\"}]}";
    }
}
