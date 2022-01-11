package io.ingestr.loader.health;

import io.ingestr.framework.service.workers.triggers.TriggerThread;
import io.ingestr.framework.service.workers.triggers.TriggersService;
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
public class TriggersHealthIndicator implements HealthIndicator {
    private static final String NAME = "triggerServices";

    private TriggersService triggersService;

    public TriggersHealthIndicator(
            TriggersService triggersService) {
        this.triggersService = triggersService;
    }

    @Override
    public Publisher<HealthResult> getResult() {
        HealthStatus healthStatus = HealthStatus.UP;

        List<HealthResult> hr = new ArrayList<>();

        if (triggersService.hasInitialised()) {
            if (!triggersService.isRunning()) {
                healthStatus = HealthStatus.DOWN;
            }

            //now look for reasons its not

            for (TriggerThread tf : triggersService.getTriggerFunctions()) {
                Map<String, String> details = new HashMap<>();
                details.put("ingestion", String.valueOf(tf.getIngestion().getIdentifier()));

                //fail the overall health for this
                if (!tf.isRunning()) {
                    healthStatus = HealthStatus.DOWN;
                }
                hr.add(
                        HealthResult
                                .builder(tf.getTrigger().getName())
                                .status(tf.isRunning() ? HealthStatus.UP : HealthStatus.DOWN)
                                .details(details)
                                .build()
                );
            }
        }
        return Flux.just(
                HealthResult.builder(NAME, healthStatus)
                        .details(hr.isEmpty() ? null : hr)
                        .build()
        );
    }
}
