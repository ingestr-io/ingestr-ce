package io.ingestr.loader.example;

import io.ingestr.framework.builders.IngestrFunctions;
import io.ingestr.framework.entities.AbstractTriggerFunction;
import io.ingestr.framework.entities.Partition;
import io.ingestr.framework.entities.TriggerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinanceTrigger extends AbstractTriggerFunction {
    @Override
    public void run(TriggerContext context) {

        //1. Trigger runs in a dedicated thread, lets run it
        while (!isShutdown()) {
            try {
                Thread.sleep(10_000);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            for (Partition partition : context.partitions()) {
                context.notify(IngestrFunctions.newTriggeredPartition(partition)
                        .property("test", "value")
                );
            }
        }
    }
}
