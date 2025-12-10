package io.pluglock.core;

import java.util.concurrent.TimeUnit;

public abstract class AbstractPLockResource implements PLockResource {
    /**
     * 锁资源持有的自动过期时间
     */
    private long intervalLeaseTime;

    @Override
    public Long acquireResource(String name, long leaseTime, TimeUnit unit, long threadId) {
        long sourceLeaseTime = leaseTime;
        if (leaseTime == -1) {
            sourceLeaseTime = 30 * 1000;
        }
        Long ttl = acquireResource(name, sourceLeaseTime, unit, threadId);
        if (ttl == null) {
            if (leaseTime == -1) {
                // 启动监控狗
                startWatchDog(name, threadId);
            } else {
                // 什么也不用做,紧做记录
                intervalLeaseTime = unit.toMillis(leaseTime);
            }
            return ttl;
        }
        return 0L;
    }

    protected abstract void startWatchDog(String name, long threadId);

    @Override
    public PLockEntry subscribe(String name) {
        return null;
    }

    @Override
    public void unsubscribe(String name) {

    }

    @Override
    public Long tryAcquireResource(String name, long threadId) {
        return 0L;
    }

    @Override
    public void releaseResource(String name, long threadId) {
        // 删除hmap中的数据
        // 发布锁释放消息

        // 清理电子续期狗
    }


    public long getIntervalLeaseTime() {
        return intervalLeaseTime;
    }
}
