# PlugLock 简单示例

这个示例展示了如何使用PlugLock分布式锁框架。

## 示例说明

1. **SimpleExample.java** - 展示了基本的锁使用方法
2. **RedisExample.java** - 展示了如何使用Redis实现的分布式锁
3. **JdbcExample.java** - 展示了如何使用JDBC实现的分布式锁

## 如何运行示例

### 编译项目

```bash
cd ../.. # 回到项目根目录
mvn clean compile
```

### 运行示例

```bash
# 运行简单示例
mvn exec:java -pl examples/example-simple -Dexec.mainClass="io.pluglock.example.simple.SimpleExample"

# 运行Redis示例（需要Redis服务器运行在本地默认端口）
mvn exec:java -pl examples/example-simple -Dexec.mainClass="io.pluglock.example.simple.RedisExample"

# 运行JDBC示例（需要MySQL数据库）
mvn exec:java -pl examples/example-simple -Dexec.mainClass="io.pluglock.example.simple.JdbcExample"
```

## 注意事项

1. 运行Redis示例前，请确保Redis服务器正在运行，默认端口为6379
2. 运行JDBC示例前，请确保MySQL数据库正在运行，并且有名为test的数据库
3. 根据实际情况修改配置参数，如数据库连接信息等

## 示例输出解释

示例会演示多个线程如何安全地竞争同一把锁，确保在任何时刻只有一个线程能够执行临界区代码。