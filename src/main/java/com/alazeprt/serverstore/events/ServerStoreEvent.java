package com.alazeprt.serverstore.events;

import java.math.BigDecimal;

import static com.alazeprt.serverstore.ServerEconomyPlugin.*;
import static com.alazeprt.serverstore.utils.StatusUtils.status;
import static org.bukkit.Bukkit.getServer;

public class ServerStoreEvent implements StoreEvent {

    @Override
    public void onEnable() {
        if(money.compareTo(new BigDecimal(store_config.getString("stop.money"))) < 0) {
            onStop();
        }
    }

    @Override
    public void onDisable() {}

    @Override
    public void onStop() {
        if(store_config.getBoolean("stop.enable")) {
            status = false;
            if(store_config.getBoolean("stop.broadcast.enable")) {
                getServer().broadcastMessage(store_config.getString("stop.broadcast.message").replace("&", "§"));
            }
        }
    }

    @Override
    public void onRestore() {
        if(store_config.getBoolean("restore.enable")) {
           status = true;
           if(store_config.getBoolean("restore.broadcast.enable")) {
               getServer().broadcastMessage(store_config.getString("restore.broadcast.message").replace("&", "§"));
           }
        }
    }

    @Override
    public void onSell() {
        if(money.compareTo(new BigDecimal(store_config.getString("stop.money"))) < 0) {
            onStop();
        }
    }

    @Override
    public void onBuy() {
        if(!status && money.compareTo(new BigDecimal(store_config.getString("restore.money"))) > 0) {
            onRestore();
        }
    }
}
