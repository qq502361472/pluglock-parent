package io.pluglock.jdbc;

import io.pluglock.core.PReentrantLock;

/**
 * 基于JDBC的可重入锁实现
 */
public class JdbcPReentrantLock extends PReentrantLock {
    
    private final JdbcHelper jdbcHelper;
    private final int expireTimeSeconds;
    
    public JdbcPReentrantLock(String lockName, JdbcHelper jdbcHelper) {
        this(lockName, jdbcHelper, 30);
    }
    
    public JdbcPReentrantLock(String lockName, JdbcHelper jdbcHelper, int expireTimeSeconds) {
        super(lockName);
        this.jdbcHelper = jdbcHelper;
        this.expireTimeSeconds = expireTimeSeconds;
    }

}