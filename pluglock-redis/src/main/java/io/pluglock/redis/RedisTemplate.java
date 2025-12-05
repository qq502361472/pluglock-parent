package io.pluglock.redis;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.function.Function;

/**
 * Redis操作模板类，用于简化Redis操作
 */
public class RedisTemplate {
    
    private final JedisConnectionFactory connectionFactory;
    
    public RedisTemplate(JedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    /**
     * 执行Redis操作
     * 
     * @param action Redis操作函数
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T execute(Function<Jedis, T> action) {
        Jedis jedis = connectionFactory.getConnection();
        try {
            return action.apply(jedis);
        } finally {
            connectionFactory.releaseConnection(jedis);
        }
    }
    
    /**
     * 尝试获取锁
     * 
     * @param key 锁的键名
     * @param value 锁的值
     * @param expireSeconds 过期时间（秒）
     * @return 是否成功获取锁
     */
    public boolean tryAcquireLock(String key, String value, int expireSeconds) {
        return execute(jedis -> {
            String result = jedis.set(key, value, "NX", "EX", expireSeconds);
            return "OK".equals(result);
        });
    }
    
    /**
     * 释放锁
     * 
     * @param key 锁的键名
     * @param value 锁的值
     * @return 是否成功释放锁
     */
    public boolean releaseLock(String key, String value) {
        return execute(jedis -> {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            return "1".equals(result.toString());
        });
    }
    
    /**
     * 检查锁是否被占用
     * 
     * @param key 锁的键名
     * @return 是否被占用
     */
    public boolean isLocked(String key) {
        return execute(jedis -> jedis.get(key) != null);
    }
}