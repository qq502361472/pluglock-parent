package io.pluglock.redis;

import io.pluglock.core.PLockEntry;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 基于Lettuce的Redis锁资源实现
 */
public class LettucePLockResource extends RedisPLockResource {
    private static final Logger logger = LoggerFactory.getLogger(LettucePLockResource.class);
    
    public LettucePLockResource() {
        super();
    }
    
    public LettucePLockResource(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }
    
    @Override
    public Long acquireResource(String name, long leaseTime, TimeUnit unit, long threadId) {
        RedisConnection<?> connection = getConnectionFactory().getConnection();
        try {
            return doAcquireResource((RedisConnection<StatefulRedisConnection<String, String>>) connection, name, leaseTime, unit, threadId);
        } finally {
            getConnectionFactory().releaseConnection(connection);
        }
    }
    
    private Long doAcquireResource(RedisConnection<StatefulRedisConnection<String, String>> connection, 
                                  String name, long leaseTime, TimeUnit unit, long threadId) {
        StatefulRedisConnection<String, String> lettuceConnection = connection.getNativeConnection();
        RedisCommands<String, String> commands = lettuceConnection.sync();
        
        // 使用Lettuce实现获取锁的逻辑
        // 这里是一个示例实现，实际实现需要根据具体需求调整
        String script = "if (redis.call('exists', KEYS[1]) == 0) then " +
                "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                "return nil; " +
                "end; " +
                "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                "return nil; " +
                "end; " +
                "return redis.call('pttl', KEYS[1]);";
        
        Object result = commands.eval(script, io.lettuce.core.ScriptOutputType.INTEGER, new String[]{name}, 
                           String.valueOf(unit.toMillis(leaseTime)), String.valueOf(threadId));
        Long ttl = (Long) result;
        return ttl == 0 ? null : ttl;
    }
    
    @Override
    public Long tryAcquireResource(String name, long threadId) {
        RedisConnection<?> connection = getConnectionFactory().getConnection();
        try {
            return doTryAcquireResource((RedisConnection<StatefulRedisConnection<String, String>>) connection, name, threadId);
        } finally {
            getConnectionFactory().releaseConnection(connection);
        }
    }
    
    private Long doTryAcquireResource(RedisConnection<StatefulRedisConnection<String, String>> connection, 
                                     String name, long threadId) {
        StatefulRedisConnection<String, String> lettuceConnection = connection.getNativeConnection();
        RedisCommands<String, String> commands = lettuceConnection.sync();
        
        // 使用Lettuce实现尝试获取锁的逻辑
        String script = "if (redis.call('exists', KEYS[1]) == 0) then " +
                "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                "return nil; " +
                "end; " +
                "return redis.call('pttl', KEYS[1]);";
        
        Object result = commands.eval(script, io.lettuce.core.ScriptOutputType.INTEGER, new String[]{name}, 
                           String.valueOf(30000), String.valueOf(threadId));
        Long ttl = (Long) result;
        return ttl == 0 ? null : ttl;
    }
    
    @Override
    public void releaseResource(String name, long threadId) {
        RedisConnection<?> connection = getConnectionFactory().getConnection();
        try {
            doReleaseResource((RedisConnection<StatefulRedisConnection<String, String>>) connection, name, threadId);
        } finally {
            getConnectionFactory().releaseConnection(connection);
        }
        super.releaseResource(name, threadId);
    }
    
    private void doReleaseResource(RedisConnection<StatefulRedisConnection<String, String>> connection, 
                                  String name, long threadId) {
        StatefulRedisConnection<String, String> lettuceConnection = connection.getNativeConnection();
        RedisCommands<String, String> commands = lettuceConnection.sync();
        
        // 使用Lettuce实现释放锁的逻辑
        String script = "if (redis.call('hexists', KEYS[1], ARGV[2]) == 0) then " +
                "return nil;" +
                "end; " +
                "local counter = redis.call('hincrby', KEYS[1], ARGV[2], -1); " +
                "if (counter > 0) then " +
                "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                "return 0; " +
                "else " +
                "redis.call('del', KEYS[1]); " +
                "redis.call('publish', KEYS[2], ARGV[3]); " +
                "return 1; " +
                "end; " +
                "return nil;";
        
        commands.eval(script, io.lettuce.core.ScriptOutputType.INTEGER, new String[]{name, getChannelName(name)}, 
                   String.valueOf(30000), String.valueOf(threadId), "1");
    }
    
    private String getChannelName(String lockName) {
        return "lock:" + lockName + ":channel";
    }
}