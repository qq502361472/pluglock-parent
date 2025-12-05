package io.pluglock.core;

/**
 * 默认锁管理器，提供更简化的API用于创建分布式锁
 */
public class DefaultLockManager {
    
    /**
     * 创建分布式锁，自动选择可用的锁工厂
     * 根据优先级顺序选择：redis > zookeeper > jdbc
     * 
     * @param name 锁名称
     * @param config 锁配置
     * @return 分布式锁实例
     */
    public static PLock createLock(String name, LockConfig config) {
        return LockManager.createLock(name, config);
    }
    
    /**
     * 创建分布式锁，使用默认配置
     * 
     * @param name 锁名称
     * @return 分布式锁实例
     */
    public static PLock createLock(String name) {
        return LockManager.createLock(name, new LockConfig());
    }
}