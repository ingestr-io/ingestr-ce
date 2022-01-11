package io.ingestr.loader.example;

import io.ingestr.framework.builders.IngestrFunctions;
import io.ingestr.framework.entities.IngestionRequest;
import io.ingestr.framework.entities.IngestionResult;
import io.ingestr.framework.entities.Offset;
import io.ingestr.framework.entities.OffsetEntry;
import io.ingestr.framework.exception.NoResultsFoundException;
import io.ingestr.framework.model.BatchJob;
import io.ingestr.framework.model.BatchJobEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Optional;

@Slf4j
public class BinanceBatchJob extends BatchJob {
    @Override
    public IngestionResult ingest(IngestionRequest request) throws NoResultsFoundException {
        log.info("Ingesting {}", request);
        logEvent(BatchJobEvent.debug("asdf"));
        try {
            Thread.sleep(RandomUtils.nextLong(1, 100));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        Optional<Offset> offset =
                request.getLastOffset();

        Integer val = 0;
        if (offset.isPresent()) {
            val = Integer.parseInt(offset.get().getOffsetValue("inc").orElse("0"));
            if (Math.random() < .3) {
                val += 1;
            }
        }

        /**
         * How to Ingestion
         */
        return IngestrFunctions.newIngestionResult(request,
                        "data-test",
                        IngestrFunctions.newOffsetKey(OffsetEntry.of("a", "b"))
                                .offsetEntry(OffsetEntry.of("inc", String.valueOf(val)))
                                .updateHash(Math.random() < .2 ? RandomStringUtils.random(5) : null))
                .meta("test", "asdf")
                .build();
    }
}
