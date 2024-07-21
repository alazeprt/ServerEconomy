package com.alazeprt.servereconomy.feature.store.utils;

import java.math.BigDecimal;

public interface DataUtils {
    void addSellAmount(String project, String player, BigDecimal bigDecimal);

    void addBuyAmount(String project, String player, BigDecimal bigDecimal);

    boolean sellPerPlayerLimit(String project, String player, BigDecimal addAmount);

    boolean buyPerPlayerLimit(String project, String player, BigDecimal addAmount);

    boolean sellTotalLimit(String project, BigDecimal addAmount);

    boolean buyTotalLimit(String project, BigDecimal addAmount);

    void resetSellData();

    void resetBuyData();
}
