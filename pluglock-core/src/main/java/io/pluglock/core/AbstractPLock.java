package io.pluglock.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 锁抽象基类，提供基础实现
 */
public abstract class AbstractPLock implements PLock {
    
    protected final String lockName;
    
    public AbstractPLock(String lockName) {
        this.lockName = lockName;
    }
    
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Distributed lock does not support Condition");
    }
    
    @Override
    public String getName() {
        return lockName;
    }
}