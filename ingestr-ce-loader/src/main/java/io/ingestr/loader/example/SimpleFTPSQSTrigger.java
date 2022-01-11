package io.ingestr.loader.example;

import io.ingestr.framework.builders.IngestrFunctions;
import io.ingestr.framework.entities.AbstractTriggerFunction;
import io.ingestr.framework.entities.TriggerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleFTPSQSTrigger extends AbstractTriggerFunction {

    @Override
    public void run(TriggerContext context) {
        //1. Establish websocket/Connection

        //2. loop
        while (!isShutdown()) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            context.notify(
                    IngestrFunctions.newTriggeredPartition("fileId", "1", "histFileId", "1").build());
        }
    }
}
