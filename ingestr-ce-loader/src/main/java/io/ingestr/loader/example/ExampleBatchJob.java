package io.ingestr.loader.example;

import io.ingestr.framework.entities.IngestionRequest;
import io.ingestr.framework.entities.IngestionResult;
import io.ingestr.framework.model.BatchJob;

public class ExampleBatchJob extends BatchJob {

    @Override
    public IngestionResult ingest(IngestionRequest request) {
        return null;
    }
}
