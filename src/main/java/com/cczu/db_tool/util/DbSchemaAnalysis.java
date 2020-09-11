package com.cczu.db_tool.util;

import com.cczu.db_tool.entity.dto.DataSourceDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public interface DbSchemaAnalysis {
    DataSource generateDataSource(DataSourceDTO dataSourceConfigDTO);

    DbTemplate checkAndGetJdbcTemplate(DataSourceDTO dataSourceConfigDTO);

    String getDbLinkSql();

    String getDbFieldSql(String databaseName, String tableName);

    String getDbCreateSql(String tableName, String fieldSql);

    String getDbInsertSql(String tableName, String fieldSql, String valueSql);

    String getDbBatchInsertSql(String tableName, String fieldSql, List<String> valueSql);

    void execute(Connection connection, String sql);

    List<Map<String, Object>> queryData(Connection connection, String sql);
}
