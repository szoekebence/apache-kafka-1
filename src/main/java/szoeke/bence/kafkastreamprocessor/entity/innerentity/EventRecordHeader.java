package szoeke.bence.kafkastreamprocessor.entity.innerentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRecordHeader {

    public Long EventId;
    public Long StartTime;
    public Long EndTime;
    public String SchemaVersion;
    public Cause Cause;
    public Long Result;
    public AppId AppId;
    public KeyIds KeyIds;

}
