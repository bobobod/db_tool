package com.cczu.db_tool.util;

import com.cczu.db_tool.exception.BusinessException;
import com.google.common.collect.Lists;
import com.mysql.cj.result.Row;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public class DbTool {
    public static DbSchemaAnalysis get(String dbType){
        String mysql = DbType.MYSQL.getValue();
        if (dbType.equals(mysql)){
            return new MysqlSchemaAnalysis();
        }else {
            throw new BusinessException("不支持该类型");
        }
    }

    public static String getSpecialKey(String dbType){
        String mysql = DbType.MYSQL.getValue();
        if (dbType.equals(mysql)){
            return "`";
        }else {
            return "";
        }
    }

    public static String transferValue(String type,String value){
        String mysql = DbType.MYSQL.getValue();
        if (type.equals(mysql)){
            return escapeSql(value);
        }else {
            return escapeSql(value);
        }
    }
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        if (!str.contains("'")) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char src = str.charAt(i);
            switch (src) {
                case '\'':
                    sb.append("''");
                    break;
                case '\"':
                case '\\':
                    sb.append('\\');
                default:
                    sb.append(src);
                    break;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<String> list = Lists.newArrayList("hello","hello2","hello3","hello4");
        System.out.println(makeFields("MYSQL", list));
        System.out.println(makeFieldValues("MYSQL", list));


    }
    public static String makeFields(String dbType, List<String> existFields){
        String specialKey = getSpecialKey(dbType);
        return existFields.stream().map(item -> specialKey + item + specialKey).collect(Collectors.joining(","));
    }
    public static String makeFieldValues(String dbType,List<String> lineData){
       return lineData.stream().map(item -> "'"+DbTool.transferValue(dbType,item)+"'").collect(Collectors.joining(","));
    }
    public static List<String> getLineData(Row jsonData, List<String> titles, Map<String,Object> defaultValues){
        List<String> lineData = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            byte[] bytes = jsonData.getBytes(i);
            Object v = new String(bytes);

            if (v == null) {
                v = defaultValues.get(titles.get(i));
            }
            if (v == null) {
                lineData.add(i, "");
            } else if (v instanceof Date) {
                lineData.add(i, (String) v);
            } else {
                lineData.add(i, String.valueOf(v));
            }
        }
        return lineData;
    }

    public static void batchInsert(
            String dbType,
            DbTemplate myJdbcTemplate,
            Map<String, String> dateformat,
            DbSchemaAnalysis dbSchemaAnalysis,
            String tableName,
            List<Row> line,
            List<String> writeFields,
            List<String> prevFields,
            Map<String, Object> defaultValues) {
        List<String> batchData = new ArrayList<>();
        for (Row one : line) {
            List<String> lineData = getLineData(one, prevFields, defaultValues);
            batchData.add(makeFieldValues(dbType, lineData));
        }
        String batchInsertSql =
                dbSchemaAnalysis.getDbBatchInsertSql(tableName, makeFields(dbType, writeFields), batchData);

        System.out.println(batchInsertSql);
        try {
            myJdbcTemplate.execute(batchInsertSql);
        } catch (Exception e) {
            int count = 0;
            for (String one : batchData) {
                String insertSql =
                        dbSchemaAnalysis.getDbInsertSql(tableName, makeFields(dbType, writeFields), one);
                try {
                    myJdbcTemplate.execute(insertSql);
                } catch (Exception e2) {

                }
                count++;
            }
        }
    }


}
