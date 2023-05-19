package io.ingestr.loader.config;

import io.ingestr.framework.kafka.KafkaQueryHandler;
import io.ingestr.framework.kafka.KafkaUtils;
import io.ingestr.framework.service.db.ExtractorDefinitionServices;
import io.ingestr.framework.service.meterregistry.MetricReporterRepository;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Factory
public class MetricsFactory {

    @Singleton
    public MetricReporterRepository metricSummaryRepository(
            ExtractorDefinitionServices extractorDefinitionServices,
            KafkaQueryHandler kafkaQueryHandler,
            @Value("${metrics.topic}") String topic,
            @Value("${metrics.partitions}") Integer partitions
    ) {
        log.info("Creating Metric Summary Repository for topic - {}", topic);

        return new MetricReporterRepository(
                extractorDefinitionServices.getExtractorDefinition().getName(),
                kafkaQueryHandler,
                topic,
                KafkaUtils.partitionForKey(extractorDefinitionServices.getExtractorDefinition().getName(), partitions)
        );
    }
}
