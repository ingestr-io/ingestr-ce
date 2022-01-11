package io.ingestr.loader.config;

import io.ingestr.framework.service.db.LoaderDefinitionServices;
import io.ingestr.framework.service.logging.EventLogger;
import io.ingestr.framework.service.logging.EventLoggerLogImpl;
import io.ingestr.framework.service.logging.store.*;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
@Factory
@Requires(property = "kafka.enabled", value = "false")
public class LoggerServiceFactory {

    @Singleton
    public EventBus eventBus() {
        return new EventBusInMemoryImpl(new ArrayBlockingQueue<>(50_000));
    }

    @Singleton
    public EventLogger loggerService(
            LoaderDefinitionServices loaderDefinitionServices,
            EventBus eventBus) {
        log.info("Initializing In Memory Logger Service with Config");
        return new EventLoggerLogImpl(
                eventBus,
                loaderDefinitionServices.getLoaderDefinition().getLoaderName()
        );
    }

    @Singleton
    public EventLogDB eventLogDB() {
        return new EventLogDB();
    }

    @Singleton
    public EventStore eventStore(EventLogDB eventLogDB) {
        return new EventStoreInMemoryImpl(eventLogDB);
    }

    @Singleton
    public EventLogRepository eventLogRepository(EventLogDB eventLogDB) {
        return new EventLogRepositoryInMemory(eventLogDB);
    }

    @Singleton
    public EventConsumer eventConsumer(EventBus eventBus, EventStore eventStore) {
        EventConsumerDefault d = new EventConsumerDefault(eventBus, eventStore);
        return d;
    }

}
