package io.pluglock.core;

/**
 * Redis操作回调接口
 * 
 * @param <T> Redis客户端连接类型
 * @param <R> 返回值类型
 */
@FunctionalInterface
public interface RedisCallback<T, R> {
    
    /**
     * 在Redis连接上执行操作
     * 
     * @param connection Redis连接对象
     * @return 操作结果
     */
    R doInRedis(T connection);
}