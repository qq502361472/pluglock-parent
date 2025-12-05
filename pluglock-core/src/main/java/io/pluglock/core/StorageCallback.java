package io.pluglock.core;

/**
 * 存储操作回调接口
 * 
 * @param <T> 存储客户端连接类型
 * @param <R> 返回值类型
 */
@FunctionalInterface
public interface StorageCallback<T, R> {
    
    /**
     * 在存储连接上执行操作
     * 
     * @param connection 存储连接对象
     * @return 操作结果
     */
    R doInStorage(T connection);
}