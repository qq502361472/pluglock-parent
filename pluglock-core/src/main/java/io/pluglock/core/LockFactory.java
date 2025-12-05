package io.pluglock.core;

/**
 * 分布式锁工厂接口，用于通过SPI机制创建不同类型的分布式锁
 */
public interface LockFactory {
    
    /**
     * 创建分布式锁实例
     * 
     * @param name 锁的名称
     * @param config 锁的配置参数
     * @return 分布式锁实例
     */
    PLock createLock(String name, LockConfig config);
    
    /**
     * 获取工厂的名称
     * 
     * @return 工厂名称
     */
    String getName();
}