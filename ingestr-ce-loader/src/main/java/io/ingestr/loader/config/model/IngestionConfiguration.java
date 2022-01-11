package io.ingestr.loader.config.model;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

@ConfigurationProperties("ingestion")
@ToString
public class IngestionConfiguration {

    /**
     * The topic in which commands will be sent
     */

    private String topicPattern;
//    = "ingestr.loaders.${loaderName}.ingestions.${topic}";


    /**
     * Kafka
     */
    private String bootstrapServers = "localhost:9092";
    private Integer partitions = 1;

    public String getTopicPattern() {
        return topicPattern;
    }

    public void setTopicPattern(String topicPattern) {
        this.topicPattern = topicPattern;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Integer getPartitions() {
        return partitions;
    }

    public void setPartitions(Integer partitions) {
        this.partitions = partitions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("topicPattern", topicPattern)
                .append("bootstrapServers", bootstrapServers)
                .append("partitions", partitions)
                .toString();
    }
}
