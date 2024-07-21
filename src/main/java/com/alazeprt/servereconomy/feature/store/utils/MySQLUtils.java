package com.alazeprt.servereconomy.feature.store.utils;

import com.alazeprt.servereconomy.ServerEconomyPlugin;

import java.math.BigDecimal;

import static com.alazeprt.servereconomy.feature.store.ServerStore.store_data;

public class MySQLUtils implements DataUtils {
    @Override
    public void addSellAmount(String project, String player, BigDecimal bigDecimal) {
        ServerEconomyPlugin.storeDatabase.createTable(StoreType.SELL, project);
        ServerEconomyPlugin.storeDatabase.insertData(StoreType.SELL, project, player, bigDecimal);
    }

    @Override
    public void addBuyAmount(String project, String player, BigDecimal bigDecimal) {
        ServerEconomyPlugin.storeDatabase.createTable(StoreType.BUY, project);
        ServerEconomyPlugin.storeDatabase.insertData(StoreType.BUY, project, player, bigDecimal);
    }

    @Override
    public boolean sellPerPlayerLimit(String project, String player, BigDecimal addAmount) {
        ServerEconomyPlugin.storeDatabase.createTable(StoreType.SELL, project);
        if(!store_data.getBoolean("sell.player_limit")) {
            return true;
        }
        BigDecimal player_amount = ServerEconomyPlugin.storeDatabase.searchData(StoreType.SELL, project, player);
        player_amount = player_amount.add(addAmount);
        return player_amount.compareTo(new BigDecimal(store_data.getString("sell." + project + ".player_limit"))) <= 0;
    }

    @Override
    public boolean buyPerPlayerLimit(String project, String player, BigDecimal addAmount) {
        ServerEconomyPlugin.storeDatabase.createTable(StoreType.SELL, project);
        if(!store_data.getBoolean("buy.player_limit")) {
            return true;
        }
        BigDecimal player_amount = ServerEconomyPlugin.storeDatabase.searchData(StoreType.BUY, project, player);
        player_amount = player_amount.add(addAmount);
        return player_amount.compareTo(new BigDecimal(store_data.getString("buy." + project + ".player_limit"))) <= 0;
    }

    @Override
    public boolean sellTotalLimit(String project, BigDecimal addAmount) {
        ServerEconomyPlugin.storeDatabase.createTable(StoreType.SELL, project);
        if(!store_data.getBoolean("sell.total_limit")) {
            return true;
        }
        BigDecimal bigDecimal = ServerEconomyPlugin.storeDatabase.searchTotal(StoreType.SELL, project);
        addAmount = addAmount.add(bigDecimal);
        return addAmount.compareTo(new BigDecimal(store_data.getString("sell." + project + ".total_limit"))) <= 0;
    }

    @Override
    public boolean buyTotalLimit(String project, BigDecimal addAmount) {
        ServerEconomyPlugin.storeDatabase.createTable(StoreType.SELL, project);
        if(!store_data.getBoolean("buy.total_limit")) {
            return true;
        }
        BigDecimal bigDecimal = ServerEconomyPlugin.storeDatabase.searchTotal(StoreType.BUY, project);
        addAmount = addAmount.add(bigDecimal);
        return addAmount.compareTo(new BigDecimal(store_data.getString("buy." + project + ".total_limit"))) <= 0;
    }

    @Override
    public void resetSellData() {
        ServerEconomyPlugin.storeDatabase.dropAllTable(StoreType.SELL);
    }

    @Override
    public void resetBuyData() {
        ServerEconomyPlugin.storeDatabase.dropAllTable(StoreType.BUY);
    }
}
