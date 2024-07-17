package com.alazeprt.servereconomy.feature.store.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabase {

    private final String address;

    private final String username;

    private final String password;

    private DataSource dataSource;

    private final String createTable = "CREATE TABLE IF NOT EXISTS servereconomy_store_???(" +
            "name VARCHAR(512) NOT NULL," +
            "money DECIMAL(12,2) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

    private final String searchPlayerData = "SELECT * FROM servereconomy_store_{project}" +
            " WHERE name = '{player}';";

    private final String changePlayerData = "UPDATE servereconomy_store_{project} SET money = {money} WHERE name = '{player}';";

    private final String searchTotal = "SELECT money FROM servereconomy_store_{project}";

    private final String deleteTable = "DROP TABLE {table};";

    private Connection connection;

    public MySQLDatabase(String host, int port, String database, String username, String password) {
        this.address = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
    }

    public void initial(String driver) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.address);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setMinimumIdle(20);
        hikariConfig.setIdleTimeout(60000);
        hikariConfig.setMaxLifetime(18000);
        hikariConfig.setPoolName("ServerEconomyPool");
        this.dataSource = new HikariDataSource(hikariConfig);
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initial(String driver, int maxPoolSize, int minIdle, int idleTimeout, int maxLifeTime) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.address);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setMinimumIdle(minIdle);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifeTime);
        hikariConfig.setPoolName("ServerEconomyPool");
        this.dataSource = new HikariDataSource(hikariConfig);
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidTableName(String tableName) {
        return tableName.matches("[a-zA-Z0-9_]+");
    }

    public void createTable(String type, String project) {
        checkConnection();
        project = type + "_" + project;
        if(!isValidTableName(project)) {
            throw new IllegalArgumentException("Invalid table name: " + project);
        }
        try {
            Statement statement = connection.createStatement();
            statement.execute(createTable.replace("???", project));
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getStorePlayerData(String type, String project, String playerName) throws SQLException {
        checkConnection();
        project = type + "_" + project;
        if(!isValidTableName(project)) {
            throw new IllegalArgumentException("Invalid table name: " + project);
        }
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(searchPlayerData.replace("{project}", project).replace("{player}", playerName));
        if (resultSet.next()) {
            int result = resultSet.getInt("money");
            resultSet.close();
            statement.close();
            return result;
        } else {
            resultSet.close();
            statement.close();
            return -1;
        }
    }

    public int getTotalData(String type, String project) throws SQLException {
        checkConnection();
        project = type + "_" + project;
        if(!isValidTableName(project)) {
            throw new IllegalArgumentException("Invalid table name: " + project);
        }
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(searchTotal.replace("{project}", project));
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        while(resultSet.next()) {
            total = total.add(BigDecimal.valueOf(resultSet.getInt("money")));
        }
        resultSet.close();
        statement.close();
        return total.intValue();
    }

    public void changePlayerData(String type, String project, String playerName, int money) {
        checkConnection();
        project = type + "_" + project;
        if(!isValidTableName(project)) {
            throw new IllegalArgumentException("Invalid table name: " + project);
        }
        List<String> table = getTables(type);
        if(!table.contains(project)) {
            createTable(type, project);
        }
        try {
            Statement statement = connection.createStatement();
            statement.execute(changePlayerData.replace("{project}", project).replace("{player}", playerName).replace("{money}", String.valueOf(money)));
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getTables(String type) {
        checkConnection();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(connection.getCatalog(), null, null, new String[] { "TABLE" });
            List<String> table = new ArrayList<>();
            while(resultSet.next()) {
                String tableName = (String) resultSet.getObject("TABLE_NAME");
                if(tableName.startsWith("servereconomy_store_" + type)) {
                    table.add(tableName);
                }
            }
            resultSet.close();
            return table;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTables(String type) {
        checkConnection();
        List<String> tables = getTables(type);
        try {
            Statement statement = connection.createStatement();
            for(String table : tables) {
                statement.execute(deleteTable.replace("{table}", table));
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            if(!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkConnection() {
        try {
            if(connection.isClosed()) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
