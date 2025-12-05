package io.pluglock.redis;

import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Collections;
import java.util.function.Function;

/**
 * 基于Lettuce的Redis操作模板类
 */
public class LettuceRedisTemplate {
    
    private final LettuceConnectionFactory connectionFactory;
    
    public LettuceRedisTemplate(LettuceConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    /**
     * 执行Redis操作
     *
     * @param action Redis操作函数
     * @param <T> 返回值类型
     * @return 操作结果
     */
    public <T> T execute(Function<RedisCommands<String, String>, T> action) {
        StatefulRedisConnection<String, String> connection = connectionFactory.getConnection();
        try {
            RedisCommands<String, String> commands = connection.sync();
            return action.apply(commands);
        } finally {
            connectionFactory.releaseConnection(connection);
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
        return execute(commands -> {
            String result = commands.set(key, value, io.lettuce.core.SetArgs.Builder.nx().ex(expireSeconds));
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
        return execute(commands -> {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = commands.eval(script, ScriptOutputType.INTEGER, new String[]{key}, value);
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
        return execute(commands -> commands.get(key) != null);
    }
}