package io.pluglock.core;

import java.util.Properties;

/**
 * 分布式锁配置类
 */
public class LockConfig {
    
    private Properties properties = new Properties();
    
    public LockConfig() {
    }
    
    public LockConfig(Properties properties) {
        this.properties = properties;
    }
    
    public Properties getProperties() {
        return properties;
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}