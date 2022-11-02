package szoeke.bence.kafkaprocessor.entity;

public class BasicBlockAggregate {

    public int failed_result;
    public int successful_result;
    public int err_starts_4;
    public int err_starts_5;
    public int err_starts_6;
    public int has_sub_cause;
    public int protocol_dns;
    public int protocol_diameter;
    public int protocol_diameter_err_starts_3;
    public int protocol_diameter_err_starts_4;
    public int protocol_diameter_err_starts_5;

    public BasicBlockAggregate(BasicBlockAggregate aggregate) {
        this.failed_result = aggregate.failed_result;
        this.successful_result = aggregate.successful_result;
        this.err_starts_4 = aggregate.err_starts_4;
        this.err_starts_5 = aggregate.err_starts_5;
        this.err_starts_6 = aggregate.err_starts_6;
        this.has_sub_cause = aggregate.has_sub_cause;
        this.protocol_dns = aggregate.protocol_dns;
        this.protocol_diameter = aggregate.protocol_diameter;
        this.protocol_diameter_err_starts_3 = aggregate.protocol_diameter_err_starts_3;
        this.protocol_diameter_err_starts_4 = aggregate.protocol_diameter_err_starts_4;
        this.protocol_diameter_err_starts_5 = aggregate.protocol_diameter_err_starts_5;
    }

    public BasicBlockAggregate() {
        this.failed_result = 0;
        this.successful_result = 0;
        this.err_starts_4 = 0;
        this.err_starts_5 = 0;
        this.err_starts_6 = 0;
        this.has_sub_cause = 0;
        this.protocol_dns = 0;
        this.protocol_diameter = 0;
        this.protocol_diameter_err_starts_3 = 0;
        this.protocol_diameter_err_starts_4 = 0;
        this.protocol_diameter_err_starts_5 = 0;
    }

    public BasicBlockAggregate setFailed_result(int failed_result) {
        this.failed_result = failed_result;
        return this;
    }

    public BasicBlockAggregate setSuccessful_result(int successful_result) {
        this.successful_result = successful_result;
        return this;
    }

    public BasicBlockAggregate setErr_starts_4(int err_starts_4) {
        this.err_starts_4 = err_starts_4;
        return this;
    }

    public BasicBlockAggregate setErr_starts_5(int err_starts_5) {
        this.err_starts_5 = err_starts_5;
        return this;
    }

    public BasicBlockAggregate setErr_starts_6(int err_starts_6) {
        this.err_starts_6 = err_starts_6;
        return this;
    }

    public BasicBlockAggregate setHas_sub_cause(int has_sub_cause) {
        this.has_sub_cause = has_sub_cause;
        return this;
    }

    public BasicBlockAggregate setProtocol_dns(int protocol_dns) {
        this.protocol_dns = protocol_dns;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter(int protocol_diameter) {
        this.protocol_diameter = protocol_diameter;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter_err_starts_3(int protocol_diameter_err_starts_3) {
        this.protocol_diameter_err_starts_3 = protocol_diameter_err_starts_3;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter_err_starts_4(int protocol_diameter_err_starts_4) {
        this.protocol_diameter_err_starts_4 = protocol_diameter_err_starts_4;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter_err_starts_5(int protocol_diameter_err_starts_5) {
        this.protocol_diameter_err_starts_5 = protocol_diameter_err_starts_5;
        return this;
    }
}
