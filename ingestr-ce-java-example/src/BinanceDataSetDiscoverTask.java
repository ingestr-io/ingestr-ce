import io.ingestr.framework.model.entities.DataSet;
import io.ingestr.framework.entities.dataset.DataSetKey;
import io.ingestr.framework.entities.discovery.DataSetDiscoverTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinanceDataSetDiscoverTask implements DataSetDiscoverTask {
    private final static Logger log = LoggerFactory.getLogger(BinanceDataSetDiscoverTask.class);

    @Override
    public void discover(DataSetDiscovererRequest request, DataSetDiscovererResult result) {
        log.info("Discovering new Binance Data Sets ...");

        result.addDataSet(IngestrFunctions.newDataSet(
                        DataSetKey.newEntry("currencyPair", "BTCUSD"),
                        DataSetKey.newEntry("resolution", "5m")
                )
                .priority(DataSet.Priority.NORMAL)
                .meta("type", "base")
                .meta("exchange", "binance")
                .tags("BTC", "crypto"));
    }
}