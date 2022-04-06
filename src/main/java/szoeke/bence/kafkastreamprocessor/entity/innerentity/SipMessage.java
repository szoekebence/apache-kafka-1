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
public class SipMessage {

    public Long Time;
    public String Direction;
    public StartLine StartLine;
    public String Interface;
    public List<NameValuePair<String>> HeaderFields;

}
