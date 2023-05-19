import io.ingestr.framework.entities.extractor.task.ExtractorResultRecord;
import io.ingestr.framework.entities.extractor.task.ExtractionRequest;
import io.ingestr.framework.entities.extractor.task.ExtractionResult;
import io.ingestr.framework.entities.dataset.Offset;
import io.ingestr.framework.exception.NoResultsFoundException;
import io.ingestr.framework.entities.extractor.task.ExtractorTaskBatchAbstract;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BinanceExtractorTaskBatchAbstract extends ExtractorTaskBatchAbstract {
    private final static Logger log = LoggerFactory.getLogger(BinanceExtractorTaskBatchAbstract.class);

    @Override
    public ExtractionResult ingest(ExtractionRequest request) throws NoResultsFoundException {
        log.info("Ingesting {}", request);
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
         * How to Extractor
         */

        return ExtractionResult
                .record(
                        ExtractorResultRecord.STRING
                                .of("asdf")
                                .addOffset("a", "b")
                                .addOffset("inc", String.valueOf(val))
                                .addMeta("test", "asdf")
                                .setUpdateHash(Math.random() < .2 ? RandomStringUtils.random(5) : null));
    }
}
