package io.ingestr.loader.config.model;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.ToString;

@ConfigurationProperties("commands")
@Data
@ToString
public class CommandGatewayConfiguration {
    private String source;
    private String context;
    /**
     * The age of the command after which it would be ignored (default: 60 seconds)
     */
    private Long commandAgeLimit = 60l;


    /**
     * Kafka Related Settings
     */
    private String bootstrapServers = "localhost:9092";
    private String topic = "extractor.commands";
    private String groupId;
    private Integer defaultPartitions = 32;


}
