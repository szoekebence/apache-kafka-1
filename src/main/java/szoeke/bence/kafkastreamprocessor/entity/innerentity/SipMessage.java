package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import java.util.List;

public class SipMessage {

    public Long Time;
    public String Direction;
    public StartLine StartLine;
    public String Interface;
    public List<NameValuePair<String>> HeaderFields;

}
