package com.alazeprt.servereconomy.store.utils;


import static com.alazeprt.servereconomy.store.ServerStore.store_config;

public class StatusUtils {

    public static boolean status = true;

    public static boolean sellStatus() {
        if(!status && (store_config.getString("stop.type").equalsIgnoreCase("STOP_SELL") ||
                store_config.getString("stop.type").equalsIgnoreCase("STOP_BOTH") ||
                store_config.getString("stop.type").equalsIgnoreCase("STOP_ALL"))) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean buyStatus() {
        if(!status && (store_config.getString("stop.type").equalsIgnoreCase("STOP_BUY") ||
                store_config.getString("stop.type").equalsIgnoreCase("STOP_BOTH") ||
                store_config.getString("stop.type").equalsIgnoreCase("STOP_ALL"))) {
            return false;
        } else {
            return true;
        }
    }
}
