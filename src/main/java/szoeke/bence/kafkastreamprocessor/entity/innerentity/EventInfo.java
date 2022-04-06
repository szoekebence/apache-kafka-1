package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInfo {

    public List<SipMessage> SipMessages;
    public List<DiameterMessage> DiameterMessages;
    public List<Object> DnsEnumMessages;

}
