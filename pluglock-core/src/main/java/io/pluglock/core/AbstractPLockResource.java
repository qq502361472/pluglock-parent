package io.pluglock.core;

import java.util.concurrent.TimeUnit;

public abstract class AbstractPLockResource implements PLockResource{
    @Override
    public Long acquireResource(String name, long leaseTime, TimeUnit unit, long threadId) {

        return 0L;
    }

    @Override
    public PLockEntry subscribe(String name) {
        return null;
    }

    @Override
    public void unsubscribe(String name) {

    }

    @Override
    public Long tryAcquireResource(String name, long threadId) {
        return 0L;
    }
}
