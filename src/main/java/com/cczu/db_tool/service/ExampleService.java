package com.cczu.db_tool.service;

import com.cczu.db_tool.entity.dto.DataSourceDTO;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public interface ExampleService {
    String get(int id, DataSourceDTO dataSourceDTO);
    void insert(DataSourceDTO dataSourceDTO,String db,String table);
}
