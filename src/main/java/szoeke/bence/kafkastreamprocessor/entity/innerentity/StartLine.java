package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartLine {

    public String Request;
    public String Response;

}
