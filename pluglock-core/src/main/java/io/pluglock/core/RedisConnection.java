package io.pluglock.core;

/**
 * Redis连接接口，用于抽象不同的Redis客户端连接
 * @param <T> Redis客户端连接类型
 */
public interface RedisConnection<T> {
    
    /**
     * 执行Redis操作
     * 
     * @param callback Redis操作回调
     * @param <R> 返回值类型
     * @return 操作结果
     */
    <R> R execute(RedisCallback<T, R> callback);
    
    /**
     * 关闭连接
     */
    void close();
    
    /**
     * 获取原始连接对象
     * 
     * @return 原始连接对象
     */
    T getNativeConnection();
}