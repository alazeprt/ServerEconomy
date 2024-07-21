package com.alazeprt.servereconomy.database.mysql;

import com.alazeprt.servereconomy.feature.store.utils.StoreType;

import java.math.BigDecimal;
import java.sql.*;

public class StoreDatabase {

    private final DatabasePool databasePool;

    private final String create = "create table if not exists ?(`name` varchar(512) not null, `money` decimal(16,4) not null);";

    private final String insert = "insert into ? values(?, ?);";

    private final String search = "select * from ? where `name` = ?;";

    private final String searchAll = "select * from ?;";

    private final String drop = "drop table ?;";

    public StoreDatabase(DatabasePool databasePool) {
        this.databasePool = databasePool;
    }

    public void createTable(StoreType type, String name) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(create);
            preparedStatement.setString(1, type.name().toLowerCase() + "_" + name);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertData(StoreType type, String name, String playerName, BigDecimal money) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setString(1, type.name().toLowerCase() + "_" + name);
            preparedStatement.setString(2, playerName);
            preparedStatement.setBigDecimal(3, money);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal searchData(StoreType type, String name, String playerName) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(search);
            preparedStatement.setString(1, type.name().toLowerCase() + "_" + name);
            preparedStatement.setString(2, playerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal bigDecimal = new BigDecimal(0);
            while(resultSet.next()) {
                bigDecimal = bigDecimal.add(resultSet.getBigDecimal("money"));
            }
            return bigDecimal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal searchTotal(StoreType type, String project) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(searchAll);
            preparedStatement.setString(1, type.name().toLowerCase() + "_" + project);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal bigDecimal = new BigDecimal(0);
            while(resultSet.next()) {
                bigDecimal = bigDecimal.add(resultSet.getBigDecimal("money"));
            }
            return bigDecimal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropAllTable(StoreType type) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("show tables;");
            while(resultSet.next()) {
                if(resultSet.getString(1).startsWith(type.name().toLowerCase() + "_")) {
                    PreparedStatement preparedStatement = connection.prepareStatement(drop);
                    preparedStatement.setString(1, resultSet.getString("name"));
                    preparedStatement.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
