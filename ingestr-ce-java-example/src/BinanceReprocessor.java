import io.ingestr.framework.entities.Reprocessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinanceReprocessor implements Reprocessor {
    private final static Logger log = LoggerFactory.getLogger(BinanceReprocessor.class);

    @Override
    public ReprocessorResponse reprocess(ReprocessorRequest request) {
        log.info("Running Binance Backfill Processor...");

        return null;
    }
}
