package com.alazeprt.servereconomy.feature.store.utils;

import com.alazeprt.servereconomy.database.MySQLDatabase;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static com.alazeprt.servereconomy.feature.store.ServerStore.store_data;

public class MySQLUtils implements DataUtils {

    private final MySQLDatabase serverEconomyMySQL;

    public MySQLUtils(MySQLDatabase serverEconomyMySQL) {
        this.serverEconomyMySQL = serverEconomyMySQL;
    }

    @Override
    public void addSellAmount(String project, String player, BigDecimal bigDecimal) {
        BigDecimal origin = null;
        try {
            origin = BigDecimal.valueOf(serverEconomyMySQL.getStorePlayerData("sell", project, player));
        } catch (SQLException e) {
            origin = BigDecimal.ZERO;
        }
        origin = origin.add(bigDecimal);
        serverEconomyMySQL.changePlayerData("sell", project, player, origin.intValue());
    }

    @Override
    public void addBuyAmount(String project, String player, BigDecimal bigDecimal) {
        BigDecimal origin = null;
        try {
            origin = BigDecimal.valueOf(serverEconomyMySQL.getStorePlayerData("buy", project, player));
        } catch (SQLException e) {
            origin = BigDecimal.ZERO;
        }
        origin = origin.add(bigDecimal);
        serverEconomyMySQL.changePlayerData("buy", project, player, origin.intValue());
    }

    @Override
    public boolean sellPerPlayerLimit(String project, String player, BigDecimal addAmount) {
        if(!store_data.getBoolean("sell.player_limit")) {
            return true;
        }
        BigDecimal player_amount = null;
        try {
            player_amount = new BigDecimal(serverEconomyMySQL.getStorePlayerData("sell", project, player));
        } catch (SQLException e) {
            player_amount = BigDecimal.ZERO;
        }
        player_amount = player_amount.add(addAmount);
        return player_amount.compareTo(new BigDecimal(store_data.getString("sell." + project + ".player_limit"))) <= 0;
    }

    @Override
    public boolean buyPerPlayerLimit(String project, String player, BigDecimal addAmount) {
        if(!store_data.getBoolean("buy.player_limit")) {
            return true;
        }
        BigDecimal player_amount = null;
        try {
            player_amount = new BigDecimal(serverEconomyMySQL.getStorePlayerData("buy", project, player));
        } catch (SQLException e) {
            player_amount = BigDecimal.ZERO;
        }
        player_amount = player_amount.add(addAmount);
        return player_amount.compareTo(new BigDecimal(store_data.getString("buy." + project + ".player_limit"))) <= 0;
    }

    @Override
    public boolean sellTotalLimit(String project, BigDecimal addAmount) {
        if(!store_data.getBoolean("sell.total_limit")) {
            return true;
        }
        List<String> tables = serverEconomyMySQL.getTables("sell");
        if(tables.isEmpty()) {
            return true;
        }
        for(String key : tables) {
            try {
                addAmount = addAmount.add(BigDecimal.valueOf(serverEconomyMySQL.getTotalData("sell", key)));
            } catch (SQLException ignored) {}
        }
        return addAmount.compareTo(new BigDecimal(store_data.getString("sell." + project + ".total_limit"))) <= 0;
    }

    @Override
    public boolean buyTotalLimit(String project, BigDecimal addAmount) {
        if(!store_data.getBoolean("buy.total_limit")) {
            return true;
        }
        List<String> tables = serverEconomyMySQL.getTables("buy");
        if(tables.isEmpty()) {
            return true;
        }
        for(String key : tables) {
            try {
                addAmount = addAmount.add(BigDecimal.valueOf(serverEconomyMySQL.getTotalData("buy", key)));
            } catch (SQLException ignored) {}
        }
        return addAmount.compareTo(new BigDecimal(store_data.getString("buy." + project + ".total_limit"))) <= 0;
    }

    @Override
    public void resetSellData() {
        serverEconomyMySQL.deleteTables("sell");
    }

    @Override
    public void resetBuyData() {
        serverEconomyMySQL.deleteTables("buy");
    }

    public void close() {
        serverEconomyMySQL.checkConnection();
    }
}
