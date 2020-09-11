package com.cczu.db_tool.util;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public enum  DbType {
    MYSQL("MYSQL");
    private String value;

    DbType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
