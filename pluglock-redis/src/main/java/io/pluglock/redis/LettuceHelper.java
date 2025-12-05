package io.pluglock.redis;

import io.pluglock.core.RedisCallback;
import io.pluglock.core.RedisConnection;
import io.pluglock.core.RedisConnectionFactory;
import io.pluglock.core.RedisHelper;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Collections;
import java.util.function.Function;

/**
 * Lettuce操作助手类，用于简化Redis操作
 */
public class LettuceHelper implements RedisHelper {
    
    private final RedisConnectionFactory connectionFactory;
    
    public LettuceHelper(RedisConnectionFactory connectionFactory) {
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
        RedisConnection<io.lettuce.core.api.StatefulRedisConnection<String, String>> connection = connectionFactory.getConnection();
        try {
            RedisCommands<String, String> commands = connection.getNativeConnection().sync();
            return action.apply(commands);
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }
    
    @Override
    public <T, R> R execute(RedisCallback<T, R> callback) {
        RedisConnection<T> connection = connectionFactory.getConnection();
        try {
            return connection.execute(callback);
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }
    
    @Override
    public boolean tryAcquireLock(String key, String value, int expireSeconds) {
        return execute(commands -> {
            RedisCommands<String, String> syncCommands = (RedisCommands<String, String>) commands;
            String result = syncCommands.set(key, value, io.lettuce.core.SetArgs.Builder.nx().ex(expireSeconds));
            return "OK".equals(result);
        });
    }
    
    @Override
    public boolean releaseLock(String key, String value) {
        return execute(commands -> {
            RedisCommands<String, String> syncCommands = (RedisCommands<String, String>) commands;
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = syncCommands.eval(script, ScriptOutputType.INTEGER, new String[]{key}, value);
            return "1".equals(result.toString());
        });
    }
    
    @Override
    public boolean isLocked(String key) {
        return execute(commands -> {
            RedisCommands<String, String> syncCommands = (RedisCommands<String, String>) commands;
            return syncCommands.get(key) != null;
        });
    }
}