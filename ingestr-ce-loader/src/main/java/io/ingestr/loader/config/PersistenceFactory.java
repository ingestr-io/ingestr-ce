package io.ingestr.loader.config;

import io.ingestr.framework.kafka.Kafka;
import io.ingestr.loader.config.model.RepositoryServiceConfig;
import io.ingestr.framework.kafka.KafkaAdminService;
import io.ingestr.framework.service.db.RepositoryService;
import io.ingestr.framework.service.db.RepositoryServiceInMemoryImpl;
import io.ingestr.framework.service.db.RepositoryServiceKafkaImpl;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
    public RepositoryService repositoryService(RepositoryServiceConfig repositoryServiceConfig) {
        log.info("Creating Repository Service for - {}", repositoryServiceConfig);

        if (StringUtils.isBlank(repositoryServiceConfig.getSource()) ||
                repositoryServiceConfig.getSource().equalsIgnoreCase("memory")) {
            return new RepositoryServiceInMemoryImpl();
        } else if (repositoryServiceConfig.getSource().equalsIgnoreCase("kafka")) {

            //setup a future which does the topic initialisation and producer setup
            CompletableFuture<Producer<String, String>> res = CompletableFuture.supplyAsync(() -> {

                //1. Initialise the topic
                Properties properties = new Properties();
                properties.put(
                        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, repositoryServiceConfig.getKafkaBootstrapServers()
                );

                try (Admin admin = Admin.create(properties)) {
                    Collection<TopicListing> topicListings = admin.listTopics().listings().get();

                    if (!kafkaAdminService.containsTopic(repositoryServiceConfig.getTopic(), topicListings)) {
                        log.info("Creating Entity Repository Topic - {}", repositoryServiceConfig.getTopic());
                        kafkaAdminService.createTopic(admin, repositoryServiceConfig.getTopic(), KafkaAdminService.DEFAULT_PARTITIONS, (short) 1, true);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

                return Kafka.producer()
                        .bootstrapServers(repositoryServiceConfig.getKafkaBootstrapServers())
                        .clientId(repositoryServiceConfig.getContext() + "-" + repositoryServiceConfig.getTopic() + "-" +
                                RandomStringUtils.randomNumeric(10))
                        .build();
            });

            RepositoryService rs = new RepositoryServiceKafkaImpl(
                    RepositoryServiceKafkaImpl.RepositoryServiceConfig.builder()
                            .kafkaBootstrapServers(repositoryServiceConfig.getKafkaBootstrapServers())
                            .context(repositoryServiceConfig.getContext())
                            .partitionCount(repositoryServiceConfig.getPartitionCount())
                            .topic(repositoryServiceConfig.getTopic())
                            .build(),
                    res);
            //load all of the entities from kafka
            rs.sync();

            return rs;
        }
        throw new UnsupportedOperationException("Did not know how to create a factory for '" + repositoryServiceConfig.getSource() + "'");
    }

}
