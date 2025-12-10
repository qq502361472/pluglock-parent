package io.pluglock.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 锁接口，继承自JDK的Lock接口
 */
public interface PLock extends Lock {

    /**
     * Acquires the lock with defined <code>leaseTime</code>.
     * Waits if necessary until lock became available.
     *
     * Lock will be released automatically after defined <code>leaseTime</code> interval.
     *
     * @param leaseTime the maximum time to hold the lock after it's acquisition,
     *        if it hasn't already been released by invoking <code>unlock</code>.
     *        If leaseTime is -1, hold the lock until explicitly unlocked.
     * @param unit the time unit
     *
     */
    void lock(long leaseTime, TimeUnit unit) throws InterruptedException;

    /**
     * 获取锁的名字
     * 
     * @return 锁的名字
     */
    String getName();
}