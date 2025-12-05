package io.pluglock.core;

/**
 * Redis操作助手接口，用于简化Redis操作
 */
public interface RedisHelper {
    
    /**
     * 执行Redis操作
     * 
     * @param callback Redis操作回调
     * @param <T> Redis客户端连接类型
     * @param <R> 返回值类型
     * @return 操作结果
     */
    <T, R> R execute(RedisCallback<T, R> callback);
    
    /**
     * 尝试获取锁
     * 
     * @param key 锁的键名
     * @param value 锁的值
     * @param expireSeconds 过期时间（秒）
     * @return 是否成功获取锁
     */
    boolean tryAcquireLock(String key, String value, int expireSeconds);
    
    /**
     * 释放锁
     * 
     * @param key 锁的键名
     * @param value 锁的值
     * @return 是否成功释放锁
     */
    boolean releaseLock(String key, String value);
    
    /**
     * 检查锁是否被占用
     * 
     * @param key 锁的键名
     * @return 是否被占用
     */
    boolean isLocked(String key);
}