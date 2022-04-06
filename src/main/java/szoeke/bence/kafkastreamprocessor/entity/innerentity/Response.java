package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response extends Request {

    public Long Result;
    public String OrigRealm;

    public Response setResult(Long result) {
        Result = result;
        return this;
    }

    public Response setOrigRealm(String origRealm) {
        OrigRealm = origRealm;
        return this;
    }
}
