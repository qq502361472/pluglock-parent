package io.pluglock.zookeeper;

import io.pluglock.core.PLock;
import io.pluglock.core.LockConfig;
import io.pluglock.core.LockFactory;

/**
 * ZooKeeper分布式锁工厂实现
 */
public class ZookeeperLockFactory implements LockFactory {
    
    @Override
    public PLock createLock(String name, LockConfig config) {
        // TODO: 实现ZooKeeper锁的创建逻辑
        throw new UnsupportedOperationException("ZooKeeper lock not implemented yet");
    }
    
    @Override
    public String getName() {
        return "zookeeper";
    }
}