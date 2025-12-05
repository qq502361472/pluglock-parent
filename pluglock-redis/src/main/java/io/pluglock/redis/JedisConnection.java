package io.pluglock.redis;

import io.pluglock.core.RedisCallback;
import io.pluglock.core.RedisConnection;
import redis.clients.jedis.Jedis;

/**
 * Jedis连接实现
 */
public class JedisConnection implements RedisConnection<Jedis> {
    
    private final Jedis jedis;
    
    public JedisConnection(Jedis jedis) {
        this.jedis = jedis;
    }
    
    @Override
    public <R> R execute(RedisCallback<Jedis, R> callback) {
        return callback.doInRedis(jedis);
    }
    
    @Override
    public void close() {
        if (jedis != null) {
            jedis.close();
        }
    }
    
    @Override
    public Jedis getNativeConnection() {
        return jedis;
    }
}