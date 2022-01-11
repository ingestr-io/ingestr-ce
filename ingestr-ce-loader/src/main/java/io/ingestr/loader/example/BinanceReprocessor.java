package io.ingestr.loader.example;

import io.ingestr.framework.entities.Reprocessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinanceReprocessor implements Reprocessor {
    @Override
    public ReprocessorResponse reprocess(ReprocessorRequest request) {
        log.info("Running Binance Backfill Processor...");

        return null;
    }
}
