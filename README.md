# PlugLock

PlugLock 是一个基于插件化设计的分布式锁框架，提供了统一的 API 接口，并支持多种后端存储实现，包括 Redis、JDBC 和 ZooKeeper。

## 特性

- 插件化架构：轻松扩展支持不同的存储后端
- 统一 API：无论使用哪种存储后端，代码保持一致
- Spring Boot 集成：提供 Starter 简化集成过程
- 高性能：针对不同存储特性进行优化
- 易于使用：简洁的 API 设计，快速上手

## 模块说明

- `pluglock-core`: 核心 API 模块，定义了分布式锁的接口规范
- `pluglock-redis`: Redis 实现模块，基于 Redis 的分布式锁实现
- `pluglock-jdbc`: JDBC 实现模块，基于数据库的分布式锁实现
- `pluglock-zookeeper`: ZooKeeper 实现模块，基于 ZooKeeper 的分布式锁实现
- `pluglock-spring-boot-starter`: Spring Boot Starter，简化在 Spring Boot 应用中的集成

## 使用方法

### Maven 依赖

```xml
<dependency>
    <groupId>io.pluglock</groupId>
    <artifactId>pluglock-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

根据不同存储选择对应的实现依赖：

```xml
<!-- 使用 Redis 实现 -->
<dependency>
    <groupId>io.pluglock</groupId>
    <artifactId>pluglock-redis</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 使用 JDBC 实现 -->
<dependency>
    <groupId>io.pluglock</groupId>
    <artifactId>pluglock-jdbc</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 使用 ZooKeeper 实现 -->
<dependency>
    <groupId>io.pluglock</groupId>
    <artifactId>pluglock-zookeeper</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 配置

在 `application.properties` 或 `application.yml` 中进行配置：

```properties
# Redis 实现配置
pluglock.type=redis
spring.redis.host=localhost
spring.redis.port=6379
```

## 示例

请参考 `examples` 目录中的示例项目：
- `example-simple`: 简单使用示例
- `example-spring-boot`: Spring Boot 集成示例
- `example-performance`: 性能测试示例

## 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目。

## 许可证

本项目使用 Apache License 2.0 许可证，详情请见 [LICENSE](LICENSE) 文件。