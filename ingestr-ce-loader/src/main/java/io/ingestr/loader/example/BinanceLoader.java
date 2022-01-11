package io.ingestr.loader.example;

import io.ingestr.framework.IngestrFunctions;
import io.ingestr.framework.entities.DataType;
import io.ingestr.framework.entities.DeregistrationMethod;
import io.ingestr.framework.entities.FieldType;
import io.ingestr.framework.entities.Ingestion;
import io.ingestr.loader.Ingestr;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinanceLoader {

    public static void main(String[] args) {
        Ingestr.build(args)
                .loaderConfiguration(IngestrFunctions.newLoaderConfiguration()
                        .concurrency(5)
                        .defaultPartitionCount(2)
                )
                .dataDescriptor(
                        IngestrFunctions.newDataDescriptor(
                                        "binance-kline",
                                        "Binance - Kline")
                                .topic("kline", 2)
                                .addPartitionKey(IngestrFunctions.newPartitionKey("currencyPair", FieldType.SINGLE, DataType.STRING)
                                        .label("Currency Pair"))
                                .addPartitionKey(IngestrFunctions.newPartitionKey("resolution", FieldType.SINGLE, DataType.STRING)
                                        .label("Resolution"))
                                .addOffsetKey(IngestrFunctions.newOffsetKey("lastUpdate", DataType.TIMESTAMP))
                                .addParameter(
                                        IngestrFunctions.newParameter("from", FieldType.SINGLE, DataType.INTEGER)
                                                .defaultValue("5")
                                )
                                .partitionRegistrator(
                                        IngestrFunctions.newPartitionRegister(BinancePartitionRegistrator::new)
                                                .schedule("0/20 * * ? * *", "UTC")
                                                .deregistrationMethod(DeregistrationMethod.DEREGISTER)
                                )
                                .reprocessor(
                                        IngestrFunctions.newReprocessor(BinanceReprocessor::new)
                                                .parameterDescriptor(
                                                        IngestrFunctions.newParameter("from", FieldType.SINGLE, DataType.DATE)
                                                                .label("From")
                                                                .build()
                                                )
                                                .parameterDescriptor(
                                                        IngestrFunctions.newParameter("to", FieldType.SINGLE, DataType.DATE)
                                                                .label("To")
                                                                .build()
                                                )
                                )
                ).ingestion(Ingestion.batch("binance-ingestor-1", "Binance - Ingestion 1", BinanceBatchJob::new)
                        .addSchedule(
                                IngestrFunctions.newSchedule(
                                                "sched-1",
                                                "Intraday",
                                                "0 * * ? * *",
                                                "UTC")
                                        .partitionFilterByTag("btc")
                        )
                        .addSchedule(
                                IngestrFunctions.newSchedule(
                                                "sched-2",
                                                "Intraday",
                                                "0/20 * * ? * *",
                                                "UTC")
                                        .setParameter(
                                                IngestrFunctions.newParameterValue("from", "1")
                                        )
                        )
                        .addTrigger(
                                IngestrFunctions.newTrigger("trigger-1", BinanceTrigger::new)
                                        .partitionFilterByTag("btc"))
                )
                .then()
                .start();

    }
}
