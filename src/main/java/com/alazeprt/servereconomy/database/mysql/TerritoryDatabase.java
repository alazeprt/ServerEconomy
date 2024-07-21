package com.alazeprt.servereconomy.database.mysql;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerritoryDatabase {

    private final DatabasePool databasePool;

    private final String create = "create table if not exists se_territory_?(`name` varchar(512) not null, `money` decimal(16,4) not null);";

    private final String drop = "drop table se_territory_?;";

    private final String insert = "insert into se_territory_? values(?, ?);";

    private final String searchAll = "select * from se_territory_?;";

    public TerritoryDatabase(DatabasePool databasePool) {
        this.databasePool = databasePool;
    }

    public void createTable(String sampleName) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(create);
            preparedStatement.setString(1, sampleName);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertData(String sampleName, String playerName, BigDecimal money) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setString(1, sampleName);
            preparedStatement.setString(2, playerName);
            preparedStatement.setBigDecimal(3, money);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getTableList() {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            Statement statement = connection.createStatement();
            List<String> tableList = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("show tables;");
            while (resultSet.next()) {
                if(!resultSet.getString(1).startsWith("se_territory_")) continue;
                tableList.add(resultSet.getString(1));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return tableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, BigDecimal> getBlacklist(String sampleName) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(searchAll);
            preparedStatement.setString(1, sampleName);
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<String, BigDecimal> blackList = new HashMap<>();
            while (resultSet.next()) {
                blackList.put(resultSet.getString("name"), resultSet.getBigDecimal("money"));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return blackList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTable(String sampleName) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(drop);
            preparedStatement.setString(1, sampleName);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
