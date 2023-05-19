import io.ingestr.framework.entities.extractor.task.ExtractorResultRecord;
import io.ingestr.framework.entities.extractor.task.ExtractionRequest;
import io.ingestr.framework.entities.extractor.task.ExtractionResult;
import io.ingestr.framework.exception.NoResultsFoundException;
import io.ingestr.framework.entities.extractor.task.ExtractorTaskBatchAbstract;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MurexIngestionExtractionTaskBatchAbstract extends ExtractorTaskBatchAbstract {
    private final static Logger log = LoggerFactory.getLogger(MurexIngestionExtractionTaskBatchAbstract.class);

    @Override
    public ExtractionResult ingest(ExtractionRequest request) throws NoResultsFoundException {

        log.info("Ingesting {}", request);

        /**
         * How to Extractor
         */
        return ExtractionResult
                .record(
                        ExtractorResultRecord.STRING
                                .of("test-data")
                                .addOffset("a", "b")
                                .addOffset("inc", "1")
                                .addMeta("test", "asdf")
                                .setUpdateHash(Math.random() < .2 ? RandomStringUtils.random(5) : null));
    }
}
