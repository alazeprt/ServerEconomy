package com.alazeprt.servereconomy.database.mysql;

import java.math.BigDecimal;
import java.sql.*;

public class GeneralDatabase {

    private final DatabasePool databasePool;

    public GeneralDatabase(DatabasePool databasePool) {
        this.databasePool = databasePool;
    }

    public BigDecimal init() {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("create table if not exists `servereconomy`(`name` varchar(255), `value` varchar(255));");
            ResultSet resultSet = statement.executeQuery("select * from servereconomy;");
            BigDecimal money = new BigDecimal(0);
            if(!resultSet.next()) {
                statement.execute("insert into servereconomy values ('money', '0');");
            } else {
                money = new BigDecimal(resultSet.getString("value"));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return money;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown(BigDecimal money) {
        try {
            Connection connection = databasePool.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement("update servereconomy set value = ? where name = 'money';");
            statement.setString(1, money.toString());
            statement.execute();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
