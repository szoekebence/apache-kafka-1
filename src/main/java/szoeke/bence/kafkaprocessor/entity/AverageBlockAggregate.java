package szoeke.bence.kafkaprocessor.entity;

public class AverageBlockAggregate {

    public float numberOfEvents;
    public float sumOfDurations;

    public AverageBlockAggregate() {
        this.numberOfEvents = 0.0f;
        this.sumOfDurations = 0.0f;
    }

    public AverageBlockAggregate setNumberOfEvents(float numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
        return this;
    }

    public AverageBlockAggregate setSumOfDurations(float sumOfDurations) {
        this.sumOfDurations = sumOfDurations;
        return this;
    }
}
