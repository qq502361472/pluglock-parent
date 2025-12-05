package io.pluglock.example.simple;

import io.pluglock.core.LockConfig;
import io.pluglock.core.LockManager;
import io.pluglock.core.PLock;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单示例演示如何使用PlugLock框架
 */
public class SimpleExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("PlugLock简单示例开始");

        // 创建锁配置
        LockConfig config = createConfig();

        // 演示基本锁的使用
        demonstrateBasicLock(config);

        // 演示并发锁的使用
        demonstrateConcurrentLock(config);

        System.out.println("PlugLock简单示例结束");
    }

    private static LockConfig createConfig() {
        Properties properties = new Properties();
        // 使用基于内存的简单锁实现（作为演示）
        properties.setProperty("lock.type", "basic");
        return new LockConfig(properties);
    }

    private static void demonstrateBasicLock(LockConfig config) {
        System.out.println("\n=== 基本锁使用演示 ===");

        try {
            // 注意：这里只是演示结构，实际上需要具体实现才能运行
            // 在真实环境中，你需要选择具体的锁实现，比如Redis或JDBC
            PLock lock = LockManager.createLock("basic", "demo-lock", config);
            System.out.println("创建锁成功: " + lock.getName());

            // 模拟锁的获取和释放
            System.out.println("尝试获取锁...");
            // lock.lock(); // 获取锁
            System.out.println("获取锁成功");

            // 执行业务逻辑
            System.out.println("执行业务逻辑...");

            System.out.println("释放锁...");
            // lock.unlock(); // 释放锁
            System.out.println("锁已释放");
        } catch (Exception e) {
            System.err.println("锁操作异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demonstrateConcurrentLock(LockConfig config) throws InterruptedException {
        System.out.println("\n=== 并发锁使用演示 ===");

        // 创建一个共享锁
        // PLock lock = LockManager.createLock("basic", "concurrent-demo-lock", config);

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
                    // lock.lock(); // 获取锁
                    System.out.println("任务 " + taskId + " 获取到锁");
                    
                    // 模拟业务处理时间
                    Thread.sleep(1000);
                    System.out.println("任务 " + taskId + " 处理完成");
                    
                    // lock.unlock(); // 释放锁
                    System.out.println("任务 " + taskId + " 释放锁");
                } catch (Exception e) {
                    System.err.println("任务 " + taskId + " 发生异常: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有任务完成
        latch.await();
        executor.shutdown();
        System.out.println("所有并发任务执行完毕");
    }
}