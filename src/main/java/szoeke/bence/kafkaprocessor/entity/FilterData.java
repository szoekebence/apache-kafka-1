package szoeke.bence.kafkaprocessor.entity;

import java.util.Set;

public class FilterData {

    public String path;
    public Set<String> values;

    public FilterData setPath(String path) {
        this.path = path;
        return this;
    }

    public FilterData setValues(Set<String> values) {
        this.values = values;
        return this;
    }
}
