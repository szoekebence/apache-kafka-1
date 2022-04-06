package szoeke.bence.kafkastreamprocessor.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import szoeke.bence.kafkastreamprocessor.entity.Event;
import szoeke.bence.kafkastreamprocessor.entity.innerentity.*;

import java.util.Collections;
import java.util.List;

public class EventSerdeUnitTest {

    @Test
    public void serializeTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EventSerializer serializer = new EventSerializer(objectMapper);
        EventDeserializer deserializer = new EventDeserializer(objectMapper);
        Event testData = generateTestData();
        byte[] serializedData = serializer.serialize(null, testData);
        Event deserializedData = deserializer.deserialize(null, serializedData);
        Assert.assertEquals(objectMapper.writeValueAsString(testData), objectMapper.writeValueAsString(deserializedData));
    }

    private Event generateTestData() {
        Event result = new Event();
        result.eventRecordHeader = new EventRecordHeader();
        result.eventRecordHeader.Result = 1L;
        result.eventRecordHeader.StartTime = 2L;
        result.eventRecordHeader.EndTime = 3L;
        result.eventRecordHeader.EventId = 4L;
        result.eventRecordHeader.SchemaVersion = "A";
        result.eventRecordHeader.AppId = getAppId();
        result.eventRecordHeader.KeyIds = getKeyIds();
        result.eventRecordHeader.Cause = getCause();
        result.eventInfo = new EventInfo();
        result.eventInfo.DiameterMessages = List.of(getDiameterMessage());
        result.eventInfo.SipMessages = List.of(getSipMessage());
        result.eventInfo.DnsEnumMessages = Collections.emptyList();
        return result;
    }

    private AppId getAppId() {
        AppId result = new AppId();
        result.NodeId = "B";
        result.PayLoad = "C";
        result.Role = "D";
        result.SwVersion = "E";
        result.Type = "F";
        return result;
    }

    private KeyIds getKeyIds() {
        KeyIds result = new KeyIds();
        result.Impi = "G";
        result.Pcv = "H";
        result.ServedUser = "I";
        result.SsId = "J";
        return result;
    }

    private Cause getCause() {
        Cause result = new Cause();
        result.ErrorCode = 5L;
        result.AdditionalInfos = List.of(getAdditionalInfo("K"));
        return result;
    }

    private AdditionalInfo getAdditionalInfo(String type) {
        AdditionalInfo result = new AdditionalInfo();
        result.Type = type;
        result.Avps = List.of(getNameLongValuePair());
        return result;
    }

    private NameValuePair<Long> getNameLongValuePair() {
        NameValuePair<Long> result = new NameValuePair<>();
        result.Name = "L";
        result.Values = List.of(6L);
        return result;
    }

    private DiameterMessage getDiameterMessage() {
        DiameterMessage result = new DiameterMessage();
        result.Cmd = "M";
        result.Direction = "N";
        result.SessionId = "O";
        result.Time = 7L;
        result.message = getMessage();
        return result;
    }

    private Message getMessage() {
        Message result = new Message();
        result.Request = getRequest();
        result.Response = getResponse();
        return result;
    }

    private Request getRequest() {
        Request result = new Request();
        result.DestRealm = "P";
        result.OrigHost = "Q";
        result.DiamAppId = 8L;
        result.AdditionalInfo = getAdditionalInfo("R");
        return result;
    }

    private Response getResponse() {
        Response result = new Response();
        result.DestRealm = "S";
        result.OrigHost = "T";
        result.DiamAppId = 9L;
        result.AdditionalInfo = getAdditionalInfo("U");
        result.Result = 10L;
        return result;
    }

    private SipMessage getSipMessage() {
        SipMessage result = new SipMessage();
        result.Direction = "V";
        result.Interface = "W";
        result.Time = 11L;
        result.HeaderFields = List.of(getNameStringValuePair());
        result.StartLine = getStartLine();
        return result;
    }

    private NameValuePair<String> getNameStringValuePair() {
        NameValuePair<String> result = new NameValuePair<>();
        result.Name = "X";
        result.Values = List.of("Y");
        return result;
    }

    private StartLine getStartLine() {
        StartLine result = new StartLine();
        result.Request = "Z";
        result.Response = "AB";
        return result;
    }
}
