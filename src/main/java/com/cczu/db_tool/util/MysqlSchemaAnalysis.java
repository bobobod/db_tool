package com.cczu.db_tool.util;

import com.alibaba.druid.filter.config.ConfigTools;
import com.cczu.db_tool.entity.dto.DataSourceDTO;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public class MysqlSchemaAnalysis extends DbSchemaAnalysisImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlSchemaAnalysis.class);
    @Override
    public DataSource generateDataSource(DataSourceDTO dataSourceDTO) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(dataSourceDTO.getHost());
        dataSource.setPort(dataSourceDTO.getPort());
        dataSource.setUser(dataSourceDTO.getUsername());
        dataSource.setPassword(decryptDruidPwd(dataSourceDTO.getPwd(),dataSourceDTO.getPublicKey()));
        dataSource.setDatabaseName(dataSourceDTO.getDbName());
        dataSource.setURL(
                String.format(
                        "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&connectTimeout=5000&socketTimeout=5000&autoReconnect=true&maxReconnects=5&failOverReadOnly=false",
                        dataSourceDTO.getHost(), dataSourceDTO.getPort(), dataSourceDTO.getDbName()));

        return dataSource;
    }

    @Override
    public String getDbLinkSql() {
        return "select 1 from dual";
    }

    @Override
    public String getDbFieldSql(String databaseName, String tableName) {
        return String.format("select column_name as field from information_schema.COLUMNS where table_name = '%s' and table_schema = '%s'", tableName, databaseName);
    }

    private String decryptDruidPwd(String pwd,String publicKey){
        String finalPwd = "";
        try {
            finalPwd = ConfigTools.decrypt(publicKey, pwd);
        }catch (Exception e){
            LOGGER.error("druid解密异常",e);
        }
        return finalPwd;
    }

    @Override
    protected String getDbType() {
        return DbType.MYSQL.getValue();
    }
}
