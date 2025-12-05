package io.pluglock.core;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 锁管理器，通过SPI机制加载和管理不同的锁工厂
 */
public class LockManager {
    
    private static final ConcurrentMap<String, LockFactory> lockFactories = new ConcurrentHashMap<>();
    
    static {
        loadLockFactories();
    }
    
    private static void loadLockFactories() {
        ServiceLoader<LockFactory> loader = ServiceLoader.load(LockFactory.class);
        for (LockFactory factory : loader) {
            lockFactories.put(factory.getName(), factory);
        }
    }
    
    /**
     * 根据类型获取锁工厂
     * 
     * @param type 锁类型（如"redis"、"jdbc"、"zookeeper"等）
     * @return 锁工厂实例
     */
    public static LockFactory getLockFactory(String type) {
        return lockFactories.get(type);
    }
    
    /**
     * 创建分布式锁
     * 
     * @param type 锁类型
     * @param name 锁名称
     * @param config 锁配置
     * @return 分布式锁实例
     */
    public static PLock createLock(String type, String name, LockConfig config) {
        LockFactory factory = getLockFactory(type);
        if (factory == null) {
            throw new IllegalArgumentException("No lock factory found for type: " + type);
        }
        return factory.createLock(name, config);
    }
    
    /**
     * 获取所有可用的锁类型
     * 
     * @return 锁类型数组
     */
    public static String[] getAvailableLockTypes() {
        return lockFactories.keySet().toArray(new String[0]);
    }
}