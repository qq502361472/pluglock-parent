package io.pluglock.core;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 连接工厂提供者，通过SPI机制加载Redis连接工厂
 */
public class ConnectionFactoryProvider {
    
    private static final ConcurrentMap<String, RedisConnectionFactory> factories = new ConcurrentHashMap<>();
    
    static {
        loadFactories();
    }
    
    private static void loadFactories() {
        ServiceLoader<RedisConnectionFactory> loader = ServiceLoader.load(RedisConnectionFactory.class);
        for (RedisConnectionFactory factory : loader) {
            factories.put(factory.getName(), factory);
        }
    }
    
    /**
     * 根据名称获取连接工厂
     * 
     * @param name 连接工厂名称
     * @return 连接工厂实例
     */
    public static RedisConnectionFactory getFactory(String name) {
        return factories.get(name);
    }
    
    /**
     * 获取所有可用的连接工厂名称
     * 
     * @return 连接工厂名称数组
     */
    public static String[] getAvailableFactoryNames() {
        return factories.keySet().toArray(new String[0]);
    }
}