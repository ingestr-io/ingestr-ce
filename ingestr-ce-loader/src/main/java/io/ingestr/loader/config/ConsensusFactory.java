package io.ingestr.loader.config;

import io.ingestr.framework.service.consensus.ConsensusService;
import io.ingestr.framework.service.consensus.ConsensusServiceInMemoryImpl;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

@Factory
public class ConsensusFactory {

    @Singleton
    @Requires(property = "consensus.source", value = "memory", defaultValue = "memory")
    public ConsensusService consensusService() {
        return new ConsensusServiceInMemoryImpl();
    }
}

