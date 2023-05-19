import io.ingestr.framework.exception.NoResultsFoundException;
import io.ingestr.framework.entities.extractor.task.ExtractorTaskBatchAbstract;
import io.ingestr.framework.entities.extractor.task.ExtractionRequest;
import io.ingestr.framework.entities.extractor.task.ExtractionResult;
import io.ingestr.framework.entities.extractor.task.ExtractorResultRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RTEIngestionExtractionTaskBatchAbstract extends ExtractorTaskBatchAbstract {
    private final static Logger log = LoggerFactory.getLogger(RTEIngestionExtractionTaskBatchAbstract.class);

    @Override
    public ExtractionResult ingest(ExtractionRequest request) throws NoResultsFoundException {

        log.info("Ingesting {}", request);

        ExtractionResult.ExtractionResultI b =
                ExtractionResult
                        .record(
                                ExtractorResultRecord.STRING
                                        .of("asdf")
                                        .addOffset("a", "1")
                                        .addDataSetKey("market_evaluation_point_id", "1")
                                        .addDataSetKey("data_type", "2")
                                        .setUpdateHash("hash"))
                        .record(ExtractorResultRecord.STRING
                                .of("f"));


        /**
         * How to Extractor
         */
        return b;
    }
}
