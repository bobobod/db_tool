package com.cczu.db_tool.service.impl;

import com.alibaba.druid.filter.config.ConfigTools;
import com.cczu.db_tool.entity.dto.DataSourceDTO;
import com.cczu.db_tool.service.ExampleService;
import com.cczu.db_tool.util.DbSchemaAnalysis;
import com.cczu.db_tool.util.DbTemplate;
import com.cczu.db_tool.util.DbTool;
import com.cczu.db_tool.util.DbType;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.runtime.ErrorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
@Service
public class ExampleServiceImpl implements ExampleService {
    private static final Logger logger = LoggerFactory.getLogger(ExampleServiceImpl.class);
    private static final String SQL = "select id,name as stu_name,age from student where id = '%s'";

    @Override
    public String get(int name, DataSourceDTO dataSourceConfig) {

        DbSchemaAnalysis dbSchemaAnalysis = DbTool.get(dataSourceConfig.getType());
        DbTemplate jdbcTemplate = dbSchemaAnalysis.checkAndGetJdbcTemplate(dataSourceConfig);
        try {
            List<Map<String, Object>> workspaceList =
                    jdbcTemplate.query(String.format(SQL, name));
            if (!CollectionUtils.isEmpty(workspaceList)) {
                Map<String, Object> map = workspaceList.iterator().next();
                return map.toString();
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                jdbcTemplate.close();
            } catch (Exception e) {
                logger.error("数据库连接关闭异常", e);
            }
        }
        return null;
    }

    @Override
    public void insert(DataSourceDTO dataSourceDTO,String db,String table) {
        DbSchemaAnalysis dbSchemaAnalysis = DbTool.get(dataSourceDTO.getType());
        DbTemplate dbTemplate = dbSchemaAnalysis.checkAndGetJdbcTemplate(dataSourceDTO);
        String dbFieldSql = dbSchemaAnalysis.getDbFieldSql(db, table);
        List<Map<String, Object>> fieldsRaw = null;
        try {
             fieldsRaw = dbTemplate.query(dbFieldSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Object> fields = fieldsRaw.stream().map(item -> item.get("field")).collect(Collectors.toList());
        List<String> list = Lists.newArrayList("31","1","hello3");
        String fieldValues = DbTool.makeFieldValues(DbType.MYSQL.getValue(), list);
        String dbInsertSql = dbSchemaAnalysis.getDbInsertSql(table, fields.toString(),fieldValues);
        System.out.println(dbInsertSql);
        try {
            dbTemplate.execute(dbInsertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExampleServiceImpl exampleService = new ExampleServiceImpl();
        int id = 1;
        String password = "HSGW3EsHsBR+I+hJuVIvndpPiNUU1sFnKtJnp+OSAc5lTuOpcX8vMXep9WOC9ZKgnWbTdQmshs9TubcuEt1hmA==";
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ4JSOoESh99/lnSZ8ANkkjCEkAs5g9k9D+93lmARyhf0Ejx2BkNlsYRQ3Z6GXUBTcs1cSLbYN0yy9sf2035ZP8CAwEAAQ==";
        DataSourceDTO dataSourceDTO = new DataSourceDTO("localhost", 3306, "root", password, publicKey, "mydb", "MYSQL");
        String s = exampleService.get(id, dataSourceDTO);
        exampleService.insert(dataSourceDTO,"mydb","student");
        System.out.println(s);
    }
}
