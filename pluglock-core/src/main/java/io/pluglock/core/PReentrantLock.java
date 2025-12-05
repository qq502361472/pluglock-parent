package io.pluglock.core;

/**
 * 可重入分布式锁接口
 */
public interface PReentrantLock extends PLock {
    
    /**
     * 获取当前线程的重入次数
     * 
     * @return 重入次数
     */
    int getHoldCount();
}