package io.pluglock.core;

import java.util.concurrent.Semaphore;

public class PLockEntry {
    private Semaphore latch = new Semaphore(0);

    public Semaphore getLatch() {
        return latch;
    }

    public void setLatch(Semaphore latch) {
        this.latch = latch;
    }
}
