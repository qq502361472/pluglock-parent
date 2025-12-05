package io.pluglock.redis;

import io.pluglock.core.DistributedLock;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 基于Redis的分布式锁实现
 */
public class RedisDistributedLock implements DistributedLock {
    
    private final String lockName;
    private final RedisTemplate redisTemplate;
    private final String lockValue;
    private final int expireTimeSeconds;
    
    public RedisDistributedLock(String lockName, RedisTemplate redisTemplate) {
        this(lockName, redisTemplate, 30);
    }
    
    public RedisDistributedLock(String lockName, RedisTemplate redisTemplate, int expireTimeSeconds) {
        this.lockName = lockName;
        this.redisTemplate = redisTemplate;
        this.lockValue = "locked-by-" + Thread.currentThread().getName();
        this.expireTimeSeconds = expireTimeSeconds;
    }
    
    @Override
    public void lock() {
        while (!tryLock()) {
            try {
                // 短暂休眠后重试
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while acquiring lock", e);
            }
        }
    }
    
    @Override
    public void lockInterruptibly() throws InterruptedException {
        while (!tryLock()) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Thread interrupted while acquiring lock");
            }
            Thread.sleep(100);
        }
    }
    
    @Override
    public boolean tryLock() {
        return redisTemplate.tryAcquireLock(lockName, lockValue, expireTimeSeconds);
    }
    
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeoutMillis = unit.toMillis(time);
        long startTime = System.currentTimeMillis();
        
        while (true) {
            if (tryLock()) {
                return true;
            }
            
            if (System.currentTimeMillis() - startTime >= timeoutMillis) {
                return false;
            }
            
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("Thread interrupted while acquiring lock");
            }
            
            Thread.sleep(100);
        }
    }
    
    @Override
    public void unlock() {
        redisTemplate.releaseLock(lockName, lockValue);
    }
    
    @Override
    public Condition newCondition() {
        // Redis分布式锁不支持Condition
        throw new UnsupportedOperationException("Redis distributed lock does not support Condition");
    }
    
    @Override
    public String getName() {
        return lockName;
    }
}