package io.ingestr.loader.config.model;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.*;

@ConfigurationProperties("logger")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoggerServiceConfig {
    private String source;
    private String loaderName;

    /**
     * Kafka Related Parameters
     */
    private String kafkaBootstrapServers = "localhost:9092";
    private String context;
    private Integer partitionCount;
    private String topic;
}
