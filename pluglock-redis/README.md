# PlugLock Redis Module

Redis module for PlugLock distributed locking framework.

## Features

- Support for both Jedis and Lettuce Redis clients
- SPI-based client selection mechanism
- Reentrant distributed locks
- Automatic lock expiration handling

## Usage

### Maven Dependency

```xml
<dependency>
    <groupId>io.pluglock</groupId>
    <artifactId>pluglock-redis</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Selecting Redis Client via SPI

PlugLock Redis supports both Jedis and Lettuce clients. To select which one to use, include only one of the following dependencies in your project:

#### For Jedis

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.0.0</version>
</dependency>
```

#### For Lettuce

```xml
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>6.1.5.RELEASE</version>
</dependency>
```

The framework automatically detects which client is available on the classpath and uses it accordingly.

### Configuration

TODO: Add configuration examples

## Implementation Details

The Redis module implements the core `PLockResource` interface and provides two specific implementations:

1. `JedisPLockResource` - Based on Jedis client
2. `LettucePLockResource` - Based on Lettuce client

The appropriate implementation is loaded via Java SPI mechanism at runtime.