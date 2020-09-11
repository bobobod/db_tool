package com.cczu.db_tool.util;

import com.cczu.db_tool.entity.dto.DataSourceDTO;
import com.cczu.db_tool.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public abstract class DbSchemaAnalysisImpl implements DbSchemaAnalysis, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DbSchemaAnalysisImpl.class);
    protected abstract String getDbType();
    @Override
    public DbTemplate checkAndGetJdbcTemplate(DataSourceDTO dataSourceDTO) {
        DataSource dataSource = generateDataSource(dataSourceDTO);
        DbTemplate template = new DbTemplate(dataSource);
        try {
            template.execute(getDbLinkSql());
        } catch (SQLException e) {
            logger.error("连接失败{}:{}",dataSourceDTO.getHost(),dataSourceDTO.getPort());
            throw new BusinessException("连接失败");
        }
        return template;
    }
    @Override
    public String getDbCreateSql(String tableName, String fieldSql) {
        return String.format("create table %s(%s)", tableName, fieldSql);
    }

    @Override
    public String getDbInsertSql(String tableName, String fieldSql, String valueSql) {
        return String.format("insert into %s (%s) values(%s)", tableName, fieldSql, valueSql);
    }

    @Override
    public String getDbBatchInsertSql(String tableName, String fieldSql, List<String> valueSql) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("insert into %s(%s) values",tableName,fieldSql));
        for (int i = 0; i < valueSql.size(); i++) {
            sb.append(String.format("(%s),",valueSql.get(i)));
        }
        // 删除最后一个逗号
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    @Override
    public void execute(Connection connection, String sql) {

    }

    @Override
    public List<Map<String, Object>> queryData(Connection connection, String sql) {
        return null;
    }
}
