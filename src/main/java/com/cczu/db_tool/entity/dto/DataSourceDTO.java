package com.cczu.db_tool.entity.dto;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public class DataSourceDTO {
    /**
     * 主机名
     */
    private String host;
    /**
     * 端口名
     */
    private Integer port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 数据库名
     */
    private String dbName;
    /**
     * db类型
     */
    private String type;

    public DataSourceDTO(String host, Integer port, String username, String pwd, String publicKey, String dbName, String type) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.pwd = pwd;
        this.publicKey = publicKey;
        this.dbName = dbName;
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
