package io.pluglock.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis连接工厂
 */
public class JedisConnectionFactory {
    
    private JedisPool jedisPool;
    
    public JedisConnectionFactory(String host, int port) {
        this(new JedisPoolConfig(), host, port, 2000);
    }
    
    public JedisConnectionFactory(JedisPoolConfig poolConfig, String host, int port, int timeout) {
        this.jedisPool = new JedisPool(poolConfig, host, port, timeout);
    }
    
    public Jedis getConnection() {
        return jedisPool.getResource();
    }
    
    public void releaseConnection(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
    
    public void destroy() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}