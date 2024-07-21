package com.alazeprt.servereconomy.database.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabasePool {
    private final String jdbcUrl;

    private final String username;

    private final String password;

    private HikariDataSource dataSource;

    public DatabasePool(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public DatabasePool(String host, int port, String database, String username, String password) {
        this("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
    }

    public void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        this.dataSource = new HikariDataSource(config);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
