package de.crazypokemondev.pixelmongenerations.pouch.api.capabilities;

import de.crazypokemondev.pixelmongenerations.pouch.common.capabilities.PouchItemHandler;

import java.util.concurrent.Callable;

public class PouchItemHandlerFactory implements Callable<IPouchItemHandler> {
    @Override
    public IPouchItemHandler call() {
        return new PouchItemHandler(100);
    }
}
