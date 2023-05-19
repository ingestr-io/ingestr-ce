import io.ingestr.framework.impl.FixedPartitionerTask;
import io.ingestr.framework.entities.DataType;
import io.ingestr.framework.entities.extractor.ExtractorTargeting;
import io.ingestr.framework.entities.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.ingestr.framework.IngestrFunctions.*;

public class Loader {
    private final static Logger log = LoggerFactory.getLogger(Loader.class);

    public static void main(String[] args) {

        io.ingestr.framework.Ingestr.build(args)
                .configuration(newConfiguration()
                        .concurrency(5)
                        .defaultPartitionCount(2)
                )
                .extractor(
                        io.ingestr.framework.entities.extractor.Extractor.create("rte")
                )
                .extractor(
                        "rte",
                        newDescriptor("adjusted-consumption", "Adjusted Consumption")
                                .addDataSetKey(newDataSetKey("market_evaluation_point_id", FieldType.SINGLE, DataType.STRING))
                                .addDataSetKey(newDataSetKey("data_type", FieldType.SINGLE, DataType.STRING)),
                        newIngestionBatch(
                                "rte-1", "RTE - BIG ADJUSTED API", RTEIngestionExtractionTaskBatchAbstract::new)
                                .targeting(ExtractorTargeting.ALL)
                                .addSchedule(
                                        newSchedule("s-1", "Faster", "0/10 * * ? * *", "UTC")
                                                .scheduleExecutionFilter((execution) -> {
                                                    return false;
                                                })
                                )
                )
                .extractor(
                        "murex",
                        newDescriptor("murex", "Murex")
                                .addDataSetKey(newDataSetKey("table", FieldType.SINGLE, DataType.STRING))
//                                .dataSetDiscovery(newDataSetDiscoverer(
//                                        StaticDiscoverer.of()
//                                                .register(
//                                                        newDataSet(DataSetKey.newEntry("table", "table_1"))
//                                                                .priority(DataSet.Priority.LOW)
//                                                                .tags("small"))
//                                                .register(
//                                                        newDataSet(DataSetKey.newEntry("table", "table_2"))
//                                                                .priority(DataSet.Priority.HIGH)
//                                                                .tags("large"))
//                                                .build()
//                                ))
                        ,
                        newIngestionBatch("murex-ing-1", "Murex Extractor", MurexIngestionExtractionTaskBatchAbstract::new)
                                .partitioner(newPartitioner("row_id", FixedPartitionerTask.of(5)))
                                .addSchedule(
                                        newSchedule("sched-1",
                                                "Intraday",
                                                "0 * * ? * *",
                                                "UTC")
                                )
                )
                .extractor(
                        "binance",
                        newDescriptor("kline", "Binance - Kline")
                                .topic("kline", 2)
                                .addDataSetKey(newDataSetKey("currencyPair", FieldType.SINGLE, DataType.STRING)
                                        .label("Currency Pair"))
                                .addDataSetKey(newDataSetKey("resolution", FieldType.SINGLE, DataType.STRING)
                                        .label("Resolution"))
                                .addOffsetKey(newOffsetKey("lastUpdate", DataType.TIMESTAMP))

//                                .dataSetDiscovery(
//                                        newDataSetDiscoverer(BinanceDataSetDiscoverer::new)
//                                                .schedule("0/20 * * ? * *", "UTC")
//                                                .deregistrationMethod(DeregistrationMethod.DEREGISTER)
//                                )
                        ,
                        newIngestionBatch("binance-ingestor-1", "Binance - Extractor 1", BinanceExtractorTaskBatchAbstract::new)
                                .addParameter(
                                        newParameter("from", FieldType.SINGLE, DataType.INTEGER)
                                                .defaultValue("5")
                                )
                                .addSchedule(
                                        newSchedule(
                                                "sched-1",
                                                "Intraday",
                                                "0 * * ? * *",
                                                "UTC")
                                                .dataSetFilterByTag("btc")
                                )
                                .addSchedule(
                                        newSchedule(
                                                "sched-2",
                                                "Intraday",
                                                "0/20 * * ? * *",
                                                "UTC")
                                                .setParameter(
                                                        newParameterValue("from", "1")
                                                )
                                )
                                .addTrigger(
                                        newTrigger("trigger-1", "Trigger 1", BinanceTrigger::new)
                                                .dataSetFilterByTag("btc"))
                                .reprocessor(
                                        newReprocessor(BinanceReprocessor::new)
                                                .extractorParameter(
                                                        newParameter("from", FieldType.SINGLE, DataType.DATE)
                                                                .label("From")
                                                                .build()
                                                )
                                                .extractorParameter(
                                                        newParameter("to", FieldType.SINGLE, DataType.DATE)
                                                                .label("To")
                                                                .build()
                                                )
                                )
                )
                .start();

    }
}
