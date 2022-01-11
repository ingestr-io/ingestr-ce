package io.ingestr.loader.config;

import io.ingestr.framework.kafka.KafkaAdminService;
import io.ingestr.framework.service.db.LoaderDefinitionServices;
import io.ingestr.framework.service.queue.*;
import io.ingestr.loader.config.model.QueueConfiguration;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Factory
@Slf4j
public class QueueFactory {
    private KafkaAdminService kafkaAdminService;
    private LoaderDefinitionServices loaderDefinitionServices;

    @Inject
    public QueueFactory(KafkaAdminService kafkaAdminService, LoaderDefinitionServices loaderDefinitionServices) {
        this.kafkaAdminService = kafkaAdminService;
        this.loaderDefinitionServices = loaderDefinitionServices;
    }

    @Singleton
    public QueueProducer queueProducer(
            QueueService queueService,
            QueueConfiguration queueConfiguration) {
        return new QueueProducerDefaultImpl(queueService);
    }

    @Singleton
    public QueueService queueBus() {
        return new QueueServiceMemoryImpl();
    }

    @Singleton
    @Inject
    public QueueConsumer queueConsumer(
            QueueService queueService,
            QueueConfiguration queueConfiguration) {
        return new QueueConsumerDefaultImpl(queueService);
    }


}
