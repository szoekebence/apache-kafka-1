package szoeke.bence.kafkaprocessor.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class KafkaStreamsConfig {

    private static final String BOOTSTRAP_SERVER_ENV_VAR = "BOOTSTRAP_SERVER";
    private static final String NUM_STREAM_THREADS_ENV_VAR = "NUM_STREAM_THREADS";
    private static final String METRICS_RECORDING_LEVEL_CONFIG_ENV_VAR = "METRICS_RECORDING_LEVEL";
    private static final String COMMIT_INTERVAL_MS_CONFIG_ENV_VAR = "COMMIT_INTERVAL_MS";

    public Properties generateConfig() {
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "kafkastreams-processor");
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(BOOTSTRAP_SERVER_ENV_VAR));
        properties.setProperty(StreamsConfig.NUM_STREAM_THREADS_CONFIG, System.getenv(NUM_STREAM_THREADS_ENV_VAR));
        setOptionalParameters(properties);
        return properties;
    }

    private static void setOptionalParameters(Properties properties) {
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
