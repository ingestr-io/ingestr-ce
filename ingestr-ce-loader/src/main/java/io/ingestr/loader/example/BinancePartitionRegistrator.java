package io.ingestr.loader.example;

import io.ingestr.framework.IngestrFunctions;
import io.ingestr.framework.entities.Partition;
import io.ingestr.framework.entities.PartitionEntry;
import io.ingestr.framework.entities.PartitionRegistrator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinancePartitionRegistrator implements PartitionRegistrator {
    @Override
    public void discover(ParitionRegistratorRequest request, ParitionRegistratorResult result) {
        log.info("Discovering new Binance Partitions ...");

        result.addPartition(IngestrFunctions.newPartition(
                                PartitionEntry.newEntry("currencyPair", "BTCUSD"),
                                PartitionEntry.newEntry("resolution", "5m")
                        )
                        .priority(Partition.Priority.NORMAL)
                        .meta("type", "base")
                        .meta("exchange", "binance")
                        .tags("BTC", "crypto")
        );
    }
}