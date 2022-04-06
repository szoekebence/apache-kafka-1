package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiameterMessage {

    public Long Time;
    public String Direction;
    public String Cmd;
    public String SessionId;
    public Message Message;
}
