package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import java.util.List;

public class EventInfo {

    public List<SipMessage> SipMessages;
    public List<DiameterMessage> DiameterMessages;
    public List<Object> DnsEnumMessages;

}
