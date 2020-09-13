package com.cczu.db_tool;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author jianzhen.yin
 * @date 2020/9/13
 */
public class DbUtil {
    /**
     * 数据库链接池
     */
    private static MysqlDataSource dataSource;
    /**
     * 为不同的线程管理连接
     */
    private static ThreadLocal<Connection> local;

    static {
        try {
            Properties properties = new Properties();
            InputStream stream = DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            properties.load(stream);
            stream.close();
            dataSource = new MysqlDataSource();
            dataSource.setPassword(properties.getProperty("password"));
            dataSource.setURL(properties.getProperty("url"));
            dataSource.setUser(properties.getProperty("username"));
            local = new ThreadLocal<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (local.get() != null) {
            return local.get();
        } else {
            Connection connection = dataSource.getConnection();
            local.set(connection);
            return connection;
        }
    }

    public static void closeConnection() {
        Connection connection = local.get();

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            local.remove();
        }
    }

    public static void main(String[] args) throws SQLException {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    Connection connection = DbUtil.getConnection();
                    System.out.println(connection);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            },"thread-"+i).start();
        }

    }
}
