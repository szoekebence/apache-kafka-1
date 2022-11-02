package szoeke.bence.kafkaprocessor.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import szoeke.bence.kafkaprocessor.entity.OperationType;
import szoeke.bence.kafkaprocessor.utility.BasicBlockAggregationSerde;

import java.util.Properties;

public class KafkaStreamsConfig {

    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private static final String NUM_STREAM_THREADS_ENV_VAR = "NUM_STREAM_THREADS";
    private static final String METRICS_RECORDING_LEVEL_CONFIG_ENV_VAR = "METRICS_RECORDING_LEVEL";
    private static final String COMMIT_INTERVAL_MS_CONFIG_ENV_VAR = "COMMIT_INTERVAL_MS";
    private final OperationType operationType;

    public KafkaStreamsConfig(OperationType operationType) {
        this.operationType = operationType;
    }

    public Properties generateConfig() {
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreams-processor");
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        properties.setProperty(StreamsConfig.NUM_STREAM_THREADS_CONFIG, System.getenv(NUM_STREAM_THREADS_ENV_VAR));
        setBasicBlockAggregationSerdes(properties);
//        setUnbiasedBlockAggregationSerdes(properties);
        setOptionalParameters(properties);
        return properties;
    }

    private void setBasicBlockAggregationSerdes(Properties properties) {
        if (operationType == OperationType.BASIC_BLOCK_AGGREGATION) {
            properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, BasicBlockAggregationSerde.class.getName());
        }
    }

//    private void setUnbiasedBlockAggregationSerdes(Properties properties) {
//        if (operationType == OperationType.UNBIASED_BLOCK_AGGREGATION) {
//            properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//            properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, UnbiasedBlockAggregationSerde.class.getName());
//        }
//    }

    private void setOptionalParameters(Properties properties) {
        String metricsRecordingLevel = System.getenv(METRICS_RECORDING_LEVEL_CONFIG_ENV_VAR);
        if (StringUtils.isNotBlank(metricsRecordingLevel)) {
            properties.setProperty(StreamsConfig.METRICS_RECORDING_LEVEL_CONFIG, metricsRecordingLevel);
        }
        String commitIntervalMs = System.getenv(COMMIT_INTERVAL_MS_CONFIG_ENV_VAR);
        if (StringUtils.isNotBlank(commitIntervalMs)) {
            properties.setProperty(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, commitIntervalMs);
        }
    }
}
