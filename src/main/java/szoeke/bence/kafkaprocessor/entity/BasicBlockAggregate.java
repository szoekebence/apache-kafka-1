package szoeke.bence.kafkaprocessor.entity;

public class BasicBlockAggregate {

    public long failed_result;
    public long successful_result;
    public long err_starts_4;
    public long err_starts_5;
    public long err_starts_6;
    public long has_sub_cause;
    public long protocol_dns;
    public long protocol_diameter;
    public long protocol_diameter_err_starts_3;
    public long protocol_diameter_err_starts_4;
    public long protocol_diameter_err_starts_5;

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

    public BasicBlockAggregate setFailed_result(long failed_result) {
        this.failed_result = failed_result;
        return this;
    }

    public BasicBlockAggregate setSuccessful_result(long successful_result) {
        this.successful_result = successful_result;
        return this;
    }

    public BasicBlockAggregate setErr_starts_4(long err_starts_4) {
        this.err_starts_4 = err_starts_4;
        return this;
    }

    public BasicBlockAggregate setErr_starts_5(long err_starts_5) {
        this.err_starts_5 = err_starts_5;
        return this;
    }

    public BasicBlockAggregate setErr_starts_6(long err_starts_6) {
        this.err_starts_6 = err_starts_6;
        return this;
    }

    public BasicBlockAggregate setHas_sub_cause(long has_sub_cause) {
        this.has_sub_cause = has_sub_cause;
        return this;
    }

    public BasicBlockAggregate setProtocol_dns(long protocol_dns) {
        this.protocol_dns = protocol_dns;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter(long protocol_diameter) {
        this.protocol_diameter = protocol_diameter;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter_err_starts_3(long protocol_diameter_err_starts_3) {
        this.protocol_diameter_err_starts_3 = protocol_diameter_err_starts_3;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter_err_starts_4(long protocol_diameter_err_starts_4) {
        this.protocol_diameter_err_starts_4 = protocol_diameter_err_starts_4;
        return this;
    }

    public BasicBlockAggregate setProtocol_diameter_err_starts_5(long protocol_diameter_err_starts_5) {
        this.protocol_diameter_err_starts_5 = protocol_diameter_err_starts_5;
        return this;
    }
}
