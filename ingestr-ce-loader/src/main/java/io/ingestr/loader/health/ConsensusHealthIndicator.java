package io.ingestr.loader.health;

import io.ingestr.framework.service.consensus.ConsensusService;
import io.ingestr.framework.service.consensus.model.Consensus;
import io.micronaut.context.annotation.Requires;
import io.micronaut.health.HealthStatus;
import io.micronaut.management.endpoint.health.HealthEndpoint;
import io.micronaut.management.health.indicator.HealthIndicator;
import io.micronaut.management.health.indicator.HealthResult;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Requires(beans = HealthEndpoint.class)
public class ConsensusHealthIndicator implements HealthIndicator {
    private static final String NAME = "consensusServices";

    private ConsensusService consensusService;

    public ConsensusHealthIndicator(ConsensusService consensusService) {
        this.consensusService = consensusService;
    }

    @Override
    public Publisher<HealthResult> getResult() {

        boolean health = true;

        List<HealthResult> hr = new ArrayList<>();

        for (Consensus con : consensusService.listConsensus()) {
            Map<String, String> details = new HashMap<>();
            details.put("lastHeartbeat", String.valueOf(con.lastHeartbeat().orElse(null)));
            details.put("leader", String.valueOf(con.leader()));
            details.put("host", String.valueOf(con.hostname()));

            if (!con.isHealthy()) {
                health = false;
            }
            hr.add(
                    HealthResult
                            .builder(con.getConsensusWorker().getName())
                            .status(con.isHealthy() ? HealthStatus.UP : HealthStatus.DOWN)
                            .details(details)
                            .build()
            );
        }


        final HealthStatus healthStatus = health ? HealthStatus.UP : HealthStatus.DOWN;

        return Flux.just(
                HealthResult.builder(NAME, healthStatus)
                        .details(hr)
                        .build()
        );
    }
}
