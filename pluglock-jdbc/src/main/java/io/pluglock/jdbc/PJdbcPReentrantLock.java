package io.pluglock.jdbc;

import io.pluglock.core.AbstractPReentrantPLock;

/**
 * 基于JDBC的可重入锁实现
 */
public class PJdbcPReentrantPLock extends AbstractPReentrantPLock {
    
    private final JdbcHelper jdbcHelper;
    private final int expireTimeSeconds;
    
    public PJdbcPReentrantPLock(String lockName, JdbcHelper jdbcHelper) {
        this(lockName, jdbcHelper, 30);
    }
    
    public PJdbcPReentrantPLock(String lockName, JdbcHelper jdbcHelper, int expireTimeSeconds) {
        super(lockName);
        this.jdbcHelper = jdbcHelper;
        this.expireTimeSeconds = expireTimeSeconds;
    }
    
    @Override
    protected boolean acquireLock(String lockValue) {
        return jdbcHelper.tryAcquireLock(lockName, lockValue, expireTimeSeconds);
    }
    
    @Override
    protected void releaseLock(String lockValue) {
        jdbcHelper.releaseLock(lockName, lockValue);
    }
}