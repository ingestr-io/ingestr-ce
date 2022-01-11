package io.ingestr.loader.config.model;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.ToString;

@ConfigurationProperties("queue")
@Data
@ToString
public class QueueConfiguration {
    private String source;


    /**
     * The topic in which commands will be sent
     */

    private String topic = "ingestr.consensus";


    /**
     * Kafka
     */
    private String bootstrapServers = "localhost:9092";
    private Integer partitions = 1;
    private Integer threadPoolLimit = 10;
}
