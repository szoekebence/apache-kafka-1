package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    public String OrigHost;
    public String DestRealm;
    public Long DiamAppId;
    public AdditionalInfo AdditionalInfo;
}
