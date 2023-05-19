package io.ingestr.loader.config;

import io.ingestr.framework.service.command.*;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class CommandFactory {

    @Singleton
    public CommandBus commandBus() {
        return new CommandBusMemoryImpl();
    }

    @Singleton
    public CommandProcessor commandProcessor(
            CommandHandler commandHandler,
            CommandBus commandBus
    ) {
        CommandProcessor cp = new CommandProcessorThreadImpl(
                5,
                commandHandler,
                commandBus);
        return cp;
    }

    @Singleton
    public CommandGateway commandGateway(
            CommandBus commandBus
    ) {
        return new CommandGatewayImpl(commandBus);
    }
}
