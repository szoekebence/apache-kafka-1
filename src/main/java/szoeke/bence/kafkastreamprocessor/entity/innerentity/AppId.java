package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppId {

    public String Type;
    public String Role;
    public String PayLoad;
    public String NodeId;
    public String SwVersion;

}
