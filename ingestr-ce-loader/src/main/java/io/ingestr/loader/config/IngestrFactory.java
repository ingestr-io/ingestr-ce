package io.ingestr.loader.config;

import io.ingestr.framework.service.producer.IngestrProducer;
import io.ingestr.framework.service.producer.IngestrProducerInMemoryImpl;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class IngestrFactory {

    @Singleton
    public IngestrProducer ingestrProducer() {
        return new IngestrProducerInMemoryImpl();
    }
}
