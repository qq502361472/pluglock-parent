package io.pluglock.redis;

import io.pluglock.core.RedisCallback;
import io.pluglock.core.RedisConnection;
import io.pluglock.core.RedisConnectionFactory;
import io.pluglock.core.RedisHelper;

import java.util.Collections;
import java.util.function.Function;

/**
 * Jedis操作助手类，用于简化Redis操作
 */
public class JedisHelper implements RedisHelper {
    
    private final RedisConnectionFactory connectionFactory;
    
    public JedisHelper(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    /**
     * 执行Redis操作
     * 
     * @param action Redis操作函数
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T execute(Function<redis.clients.jedis.Jedis, T> action) {
        RedisConnection<redis.clients.jedis.Jedis> connection = connectionFactory.getConnection();
        try {
            return action.apply(connection.getNativeConnection());
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }
    
    @Override
    public <T, R> R execute(io.pluglock.core.RedisCallback<T, R> callback) {
        RedisConnection<T> connection = connectionFactory.getConnection();
        try {
            return connection.execute(callback);
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }
    
    @Override
    public boolean tryAcquireLock(String key, String value, int expireSeconds) {
        return execute(jedis -> {
            String result = jedis.set(key, value, "NX", "EX", expireSeconds);
            return "OK".equals(result);
        });
    }
    
    @Override
    public boolean releaseLock(String key, String value) {
        return execute(jedis -> {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            return "1".equals(result.toString());
        });
    }
    
    @Override
    public boolean isLocked(String key) {
        return execute(jedis -> jedis.get(key) != null);
    }
}