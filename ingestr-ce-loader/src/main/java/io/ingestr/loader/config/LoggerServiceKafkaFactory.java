package io.ingestr.loader.config;

import io.ingestr.framework.service.logging.config.AbstractLoggerServiceKafkaFactory;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Factory
@Requires(property = "kafka.enabled", value = "true")
public class LoggerServiceKafkaFactory extends AbstractLoggerServiceKafkaFactory {


}
