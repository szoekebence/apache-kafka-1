package szoeke.bence.kafkaprocessor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szoeke.bence.kafkaprocessor.entity.innerentity.EventInfo;
import szoeke.bence.kafkaprocessor.entity.innerentity.EventRecordHeader;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable {

    public EventRecordHeader eventRecordHeader;
    public EventInfo eventInfo;

}
