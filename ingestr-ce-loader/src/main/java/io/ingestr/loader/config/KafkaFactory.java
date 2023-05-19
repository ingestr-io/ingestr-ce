package io.ingestr.loader.config;

import io.ingestr.kafkaserver.KafkaEmbedded;
import io.micronaut.context.annotation.Requires;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Requires(property = "kafka.enabled", value = "false")
public class KafkaFactory {

    public KafkaEmbedded kafkaEmbedded() {
        try {
            KafkaEmbedded ke = new KafkaEmbedded();
            ke.start();
            return ke;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
