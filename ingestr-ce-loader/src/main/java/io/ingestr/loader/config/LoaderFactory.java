package io.ingestr.loader.config;

import io.ingestr.framework.service.lock.LoaderLock;
import io.ingestr.framework.service.lock.LoaderLockImpl;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

import java.util.concurrent.locks.ReentrantLock;

@Factory
public class LoaderFactory {

    /**
     * Creates a LoaderLock instances that uses the HazelCast Distributed lock mechanism
     * This ensures that only a single Loader Thread is able to run at a time
     *
     * @return
     */
    @Singleton
    public LoaderLock loaderLock() {
        return new LoaderLockImpl(new ReentrantLock());
    }
}
