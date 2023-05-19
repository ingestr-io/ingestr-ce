package io.ingestr.loader.config;

import io.ingestr.framework.kafka.Kafka;
import io.ingestr.framework.service.db.ExtractorDefinitionServices;
import io.ingestr.framework.kafka.KafkaAdminService;
import io.ingestr.framework.service.db.RepositoryService;
import io.ingestr.framework.service.db.RepositoryServiceKafkaImpl;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.Producer;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Factory
@Slf4j
public class PersistenceFactory {
    private KafkaAdminService kafkaAdminService;

    @Inject
    public PersistenceFactory(KafkaAdminService kafkaAdminService) {
        this.kafkaAdminService = kafkaAdminService;
    }

    @Singleton
    public RepositoryService repositoryService(
            ExtractorDefinitionServices extractorDefinitionServices,
            @Value("${kafka.bootstrapServers}") String kafkaBootstrapServer,
            @Value("${repository.topic}") String repositoryTopic,
            @Value("${repository.partitions}") Integer partitions
    ) {
        log.info("Creating Repository Service");

        //setup a future which does the topic initialisation and producer setup
        CompletableFuture<Producer<String, String>> res = CompletableFuture.supplyAsync(() -> {

            //1. Initialise the topic
            Properties properties = new Properties();
            properties.put(
                    AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer
            );

            try (Admin admin = Admin.create(properties)) {
                Collection<TopicListing> topicListings = admin.listTopics().listings().get();

                if (!kafkaAdminService.containsTopic(repositoryTopic, topicListings)) {
                    log.info("Creating Entity Repository Topic - {}", repositoryTopic);
                    kafkaAdminService.createTopic(admin, repositoryTopic, partitions, (short) 1, true);
                }
                log.info("Finished Creating Repository Topic - {}", repositoryTopic);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }


            return Kafka.producer()
                    .bootstrapServers(kafkaBootstrapServer)
                    .clientId(
                            extractorDefinitionServices.getExtractorDefinition().getName() + "-" + repositoryTopic + "-" +
                                    RandomStringUtils.randomNumeric(10))
                    .build();
        });

        RepositoryService rs = new RepositoryServiceKafkaImpl(
                RepositoryServiceKafkaImpl.RepositoryServiceConfig.builder()
                        .kafkaBootstrapServers(kafkaBootstrapServer)
                        .context(extractorDefinitionServices.getExtractorDefinition().getName())
                        .partitionCount(partitions)
                        .topic(repositoryTopic)
                        .build(),
                res);
        //load all of the entities from kafka
        rs.sync();

        return rs;
    }
}
