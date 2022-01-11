package io.ingestr.loader;

import io.ingestr.framework.builders.IngestionBatchBuilder;
import io.ingestr.framework.builders.IngestionStreamBuilder;
import io.ingestr.framework.entities.*;
import io.ingestr.framework.service.LoaderService;
import io.ingestr.framework.service.db.LoaderDefinitionServices;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;


@Slf4j
public final class Ingestr {
    private final LoaderDefinition loaderDefinition;
    private final String[] args;

    private Ingestr(String loaderName, String loaderVersion, String... args) {
        this.loaderDefinition = new LoaderDefinition(loaderName, loaderVersion);
        this.args = args;
    }

    public static Ingestr build(String... args) {

        Properties properties = new Properties();
        //read the loader name definition from the build.properties file

        try {
            properties.load(Ingestr.class.getResourceAsStream("/build.properties"));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return new Ingestr(properties.getProperty("loader.name", "LOCAL"), properties.getProperty("loader.version", "LATEST"), args);
    }

    public Ingestr config(ConfigurationDescriptor.ConfigurationDescriptorBuilder configurationDescriptorBuilder) {
        this.loaderDefinition.getConfigurations().add(configurationDescriptorBuilder.build());
        return this;
    }

    public DataDescriptorIngestr dataDescriptor(DataDescriptor dataDescriptor) {
        this.loaderDefinition.getDataDescriptors().add(dataDescriptor);
        return new DataDescriptorIngestr(this, dataDescriptor);
    }

    public DataDescriptorIngestr dataDescriptor(DataDescriptor.DataDescriptorBuilder dataDescriptor) {
        return dataDescriptor(dataDescriptor.build());
    }

    public Ingestr loaderConfiguration(LoaderConfiguration.LoaderConfigurationBuilder loaderConfigurationBuilder) {
        this.loaderDefinition.setLoaderConfiguration(loaderConfigurationBuilder.build());
        return this;
    }


    /**
     * Starts the Loader thread
     */
    public void start() {
        System.out.println("\n" +
                "  ___                       _                        ____ _____ \n" +
                " |_ _|_ __   __ _  ___  ___| |_ ___  _ __           / ___| ____|\n" +
                "  | || '_ \\ / _` |/ _ \\/ __| __/ _ \\| '__|  _____  | |   |  _|  \n" +
                "  | || | | | (_| |  __/\\__ \\ || (_) | |    |_____| | |___| |___ \n" +
                " |___|_| |_|\\__, |\\___||___/\\__\\___/|_|             \\____|_____|\n" +
                "            |___/                                               \n");
        System.out.println("   (Ingestr CE v1.0.1)");
        System.out.println("==============================================================");


        //Setup loader definition from the input provided
        LoaderDefinitionServices loaderDefinitionServices = new LoaderDefinitionServices(loaderDefinition);
        loaderDefinitionServices.validate();

        //startup micronaut
        ApplicationContext app = Micronaut.build(args).banner(false).singletons(loaderDefinitionServices).start();

        //startup the LoaderService
        LoaderService loaderService = app.getBean(LoaderService.class);
        loaderService.start();


        log.info("Ingestr Loader Stopped!");
    }


    public static class DataDescriptorIngestr {
        private Ingestr ingestr;
        private DataDescriptor dataDescriptor;

        private DataDescriptorIngestr(Ingestr ingestr, DataDescriptor dataDescriptor) {
            this.ingestr = ingestr;
            this.dataDescriptor = dataDescriptor;
        }

        public DataDescriptorIngestr ingestion(Ingestion ingestion) {
            ingestion.setDataDescriptorIdentifier(dataDescriptor.getIdentifier());
            ingestr.loaderDefinition.getIngestions().add(ingestion);
            return this;
        }

        public DataDescriptorIngestr ingestion(IngestionBatchBuilder ingestion) {
            return ingestion(ingestion.build());
        }

        public DataDescriptorIngestr ingestion(IngestionStreamBuilder ingestion) {
            return ingestion(ingestion.build());
        }

        public Ingestr then() {
            return ingestr;
        }
    }
}
