package io.pluglock.core;

/**
 * Redis连接工厂接口，用于SPI机制加载不同的连接池实现
 */
public interface RedisConnectionFactory {
    
    /**
     * 获取Redis连接
     * 
     * @return Redis连接对象
     */
    RedisConnection getConnection();
    
    /**
     * 释放Redis连接
     * 
     * @param connection Redis连接对象
     */
    void releaseConnection(RedisConnection connection);
    
    /**
     * 销毁连接池
     */
    void destroy();
    
    /**
     * 获取连接工厂的名称
     * 
     * @return 连接工厂名称
     */
    String getName();
}