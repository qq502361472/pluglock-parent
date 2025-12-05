package io.pluglock.example.simple;

import io.pluglock.core.LockConfig;
import io.pluglock.core.LockManager;
import io.pluglock.core.PLock;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JDBC锁示例演示如何使用PlugLock框架的JDBC实现
 */
public class JdbcExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("PlugLock JDBC示例开始");

        // 创建JDBC锁配置
        LockConfig config = createJdbcConfig();

        // 演示JDBC锁的使用
        demonstrateJdbcLock(config);

        System.out.println("PlugLock JDBC示例结束");
    }

    private static LockConfig createJdbcConfig() {
        Properties properties = new Properties();
        properties.setProperty("jdbc.url", "jdbc:mysql://localhost:3306/test");
        properties.setProperty("jdbc.username", "root");
        properties.setProperty("jdbc.password", "password");
        properties.setProperty("jdbc.lock.type", "basic");
        return new LockConfig(properties);
    }

    private static void demonstrateJdbcLock(LockConfig config) throws InterruptedException {
        System.out.println("\n=== JDBC锁使用演示 ===");

        try {
            // 创建JDBC锁
            PLock lock = LockManager.createLock("jdbc", "jdbc-demo-lock", config);
            System.out.println("创建JDBC锁成功: " + lock.getName());

            // 创建线程池
            ExecutorService executor = Executors.newFixedThreadPool(3);
            CountDownLatch latch = new CountDownLatch(3);

            // 启动3个并发线程
            for (int i = 0; i < 3; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        System.out.println("任务 " + taskId + " 开始执行");
                        
                        // 尝试获取锁
                        System.out.println("任务 " + taskId + " 尝试获取锁");
                        lock.lock(); // 获取锁
                        System.out.println("任务 " + taskId + " 获取到锁");
                        
                        // 模拟业务处理时间
                        Thread.sleep(2000);
                        System.out.println("任务 " + taskId + " 处理完成");
                        
                        lock.unlock(); // 释放锁
                        System.out.println("任务 " + taskId + " 释放锁");
                    } catch (Exception e) {
                        System.err.println("任务 " + taskId + " 发生异常: " + e.getMessage());
                        e.printStackTrace();
                    } catch (Throwable t) {
                        System.err.println("任务 " + taskId + " 发生严重错误: " + t.getMessage());
                        t.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            // 等待所有任务完成
            latch.await();
            executor.shutdown();
            System.out.println("所有JDBC并发任务执行完毕");
        } catch (Exception e) {
            System.err.println("JDBC锁操作异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}