package io.pluglock.core;

/**
 * 存储操作接口，用于抽象不同存储后端的操作
 * 
 * @param <T> 存储客户端连接类型
 */
public interface StorageOperation<T> {
    
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
    
    /**
     * 执行存储操作
     * 
     * @param callback 存储操作回调
     * @param <R> 返回值类型
     * @return 操作结果
     */
    <R> R execute(StorageCallback<T, R> callback);
}