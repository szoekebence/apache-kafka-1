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
public class AdditionalInfo {

    public String Type;
    public List<NameValuePair<Long>> Avps;

}
