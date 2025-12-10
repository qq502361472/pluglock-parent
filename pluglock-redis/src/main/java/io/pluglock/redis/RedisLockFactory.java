package io.pluglock.redis;

import io.pluglock.core.LockConfig;
import io.pluglock.core.LockFactory;
import io.pluglock.core.PLock;

/**
 * Redis分布式锁工厂实现
 */
public class RedisLockFactory implements LockFactory {
    @Override
    public PLock createLock(String name, LockConfig config) {
        RedisPLockResource lockResource = new RedisPLockResource();
        // TODO: 实现PLock的具体创建逻辑
        return null;
    }

    @Override
    public String getName() {
        return "redis";
    }
}