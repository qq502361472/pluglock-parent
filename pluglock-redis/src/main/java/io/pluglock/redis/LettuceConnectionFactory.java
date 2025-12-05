package io.pluglock.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * Lettuce连接工厂
 */
public class LettuceConnectionFactory {
    
    private final RedisClient redisClient;
    private final String redisUri;
    
    public LettuceConnectionFactory(String host, int port) {
        this.redisUri = "redis://" + host + ":" + port;
        this.redisClient = RedisClient.create(this.redisUri);
    }
    
    public StatefulRedisConnection<String, String> getConnection() {
        return redisClient.connect();
    }
    
    public RedisCommands<String, String> getSyncCommands() {
        return redisClient.connect().sync();
    }
    
    public void releaseConnection(StatefulRedisConnection<String, String> connection) {
        if (connection != null) {
            connection.close();
        }
    }
    
    public void destroy() {
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }
}