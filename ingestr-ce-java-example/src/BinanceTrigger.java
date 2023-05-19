
import io.ingestr.framework.entities.AbstractTriggerFunction;
import io.ingestr.framework.model.entities.DataSet;
import io.ingestr.framework.entities.TriggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinanceTrigger extends AbstractTriggerFunction {
    private final static Logger log = LoggerFactory.getLogger(BinanceTrigger.class);

    @Override
    public void run(TriggerContext context) {

        //1. Trigger runs in a dedicated thread, lets run it
        while (!isShutdown()) {
            try {
                Thread.sleep(10_000);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            for (DataSet dataSet : context.dataSets()) {
                context.notify(IngestrFunctions.newTriggeredDataSet(dataSet)
                        .property("test", "value")
                );
            }
        }
    }
}
