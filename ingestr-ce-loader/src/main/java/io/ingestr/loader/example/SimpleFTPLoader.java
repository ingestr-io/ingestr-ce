package io.ingestr.loader.example;

import io.ingestr.framework.entities.*;
import io.ingestr.framework.IngestrFunctions;
import io.ingestr.loader.Ingestr;

public class SimpleFTPLoader {

    public static void main(String[] args) {
        Ingestr.build(args)
                .config(IngestrFunctions.newConfig("username", FieldType.SINGLE, DataType.STRING))
                .config(IngestrFunctions.newConfig("password", FieldType.PASSWORD, DataType.STRING))
                .dataDescriptor(
                        IngestrFunctions.newDataDescriptor("refinitiv-ts", "Refinitiv - TimeSeries")
                                .topic("timeseries")
                                .addPartitionKey(IngestrFunctions.newPartitionKey("fileId", FieldType.SINGLE, DataType.STRING)
                                        .label("File Id"))
                                .addPartitionKey(IngestrFunctions.newPartitionKey("histFileId", FieldType.SINGLE, DataType.STRING)
                                        .label("Hist File Id"))
                                .addOffsetKey(IngestrFunctions.newOffsetKey("lastUpdate", DataType.TIMESTAMP))
                                .addParameter(
                                        IngestrFunctions.newParameter("area", FieldType.CHOICE, DataType.STRING)
                                                .defaultValue("DE")
                                                .allowedValues("DE", "EN", "FR")
                                                .label("Country")
                                )
                                .partitionRegistrator(
                                        IngestrFunctions.newPartitionRegister(
                                                () -> (request, result) ->
                                                        result.addPartition(
                                                                IngestrFunctions.newPartition()
                                                                        .partitionEntry(PartitionEntry.newEntry("fileId", "1"))
                                                                        .partitionEntry(PartitionEntry.newEntry("histFileId", "2"))
                                                        )
                                        )
                                )
                ).ingestion(Ingestion.batch(
                                "ref-ing-1",
                                "Refinitiv - Ingestion 1",
                                SimpleFTPBatchJob::new)
                        .addSchedule(
                                IngestrFunctions.newSchedule("sched-1", "Intraday", "0 * * ? * *", "UTC")
                                        .setParameter(IngestrFunctions.newParameterValue("from", "2"))
                                        .setParameter(IngestrFunctions.newParameterValue("to", "3"))
                        )
                        .addTrigger(IngestrFunctions.newTrigger("trigger-1", SimpleFTPSQSTrigger::new))
                )
                .ingestion(
                        Ingestion.batch("ref-ing-2", "Refinitiv - Ingestion 2",
                                SimpleFTPBatchJob::new)
                )
                .then()
                .start();

    }
}
