package io.pluglock.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 锁接口，继承自JDK的Lock接口
 */
public interface Lock extends Lock {
    
    /**
     * 尝试获取锁，带超时时间
     * 
     * @param time 超时时间
     * @param unit 时间单位
     * @return 是否成功获取锁
     * @throws InterruptedException 如果线程被中断
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    
    /**
     * 释放锁
     */
    void unlock();
    
    /**
     * 获取锁的名字
     * 
     * @return 锁的名字
     */
    String getName();
}