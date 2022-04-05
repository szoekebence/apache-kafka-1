package szoeke.bence.kafkastreamprocessor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import szoeke.bence.kafkastreamprocessor.entity.innerentity.EventInfo;
import szoeke.bence.kafkastreamprocessor.entity.innerentity.EventRecordHeader;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable {

    public EventRecordHeader eventRecordHeader;
    public EventInfo eventInfo;

}
