package io.ingestr.loader.config.model;


import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.*;

@ConfigurationProperties("repository")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepositoryServiceConfig {
    private String source;

    /**
     * Kafka Related Parameters
     */
    private String kafkaBootstrapServers = "localhost:9092";
    private String context;
    private Integer partitionCount;
    private String topic;

}
