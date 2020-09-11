package com.cczu.db_tool.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public class DbTemplate implements Serializable {
    private static final Logger LOGGER =  LoggerFactory.getLogger(DbTemplate.class);
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;
    private Connection connection;

    public DbTemplate() {
    }

    public DbTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection connect() throws SQLException {
        if (connection == null) {
            this.connection = dataSource.getConnection();
        }
        return connection;
    }

    public void execute(String sql) throws SQLException {
        if (connection == null) {
            connect();
        }
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } finally {
            statement.close();
        }
    }

    public void close() throws SQLException {
        if (connection != null){
            connection.close();
        }
    }

    public List<Map<String,Object>> query(String sql) throws SQLException {
        if (connection == null){
            connect();
        }
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<String> fields = new ArrayList<>();
        for (int i = 1; i <= columnCount ; i++) {
            try {
                fields.add(metaData.getColumnLabel(i));
            }catch (Exception e){
                LOGGER.error("查询异常");
            }
        }
        while (resultSet.next()){
            Map<String, Object> map = new HashMap<>();
            for (String key : fields) {
                try {
                    map.put(key,resultSet.getObject(key));
                }catch (Exception e){
                    LOGGER.error("查询异常");
                }
            }
            result.add(map);
        }
        return result;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null){
            connection.close();
        }
    }
}
