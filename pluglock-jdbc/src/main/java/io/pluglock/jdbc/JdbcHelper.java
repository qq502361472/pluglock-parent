package io.pluglock.jdbc;

import io.pluglock.core.StorageCallback;
import io.pluglock.core.StorageOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC操作助手类
 */
public class JdbcHelper implements StorageOperation<Connection> {
    
    private final JdbcConnectionFactory connectionFactory;
    
    public JdbcHelper(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    @Override
    public <R> R execute(StorageCallback<Connection, R> callback) {
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            return callback.doInStorage(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error executing JDBC operation", e);
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }
    
    @Override
    public boolean tryAcquireLock(String key, String value, int expireSeconds) {
        return execute(connection -> {
            try {
                // 检查锁是否已存在
                String checkSql = "SELECT COUNT(*) FROM distributed_lock WHERE lock_key = ? AND expire_time > ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkSql);
                checkStmt.setString(1, key);
                checkStmt.setLong(2, System.currentTimeMillis());
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                rs.close();
                checkStmt.close();
                
                if (count > 0) {
                    return false; // 锁已被占用
                }
                
                // 插入新锁
                String insertSql = "INSERT INTO distributed_lock (lock_key, lock_value, expire_time) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setString(1, key);
                insertStmt.setString(2, value);
                insertStmt.setLong(3, System.currentTimeMillis() + expireSeconds * 1000);
                int result = insertStmt.executeUpdate();
                insertStmt.close();
                
                return result > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Error acquiring lock", e);
            }
        });
    }
    
    @Override
    public boolean releaseLock(String key, String value) {
        return execute(connection -> {
            try {
                String sql = "DELETE FROM distributed_lock WHERE lock_key = ? AND lock_value = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, key);
                stmt.setString(2, value);
                int result = stmt.executeUpdate();
                stmt.close();
                return result > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Error releasing lock", e);
            }
        });
    }
    
    @Override
    public boolean isLocked(String key) {
        return execute(connection -> {
            try {
                String sql = "SELECT COUNT(*) FROM distributed_lock WHERE lock_key = ? AND expire_time > ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, key);
                stmt.setLong(2, System.currentTimeMillis());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count > 0;
            } catch (SQLException e) {
                throw new RuntimeException("Error checking lock status", e);
            }
        });
    }
}