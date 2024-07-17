package com.alazeprt.servereconomy.feature.store;

import com.alazeprt.servereconomy.ServerEconomyPlugin;
import com.alazeprt.servereconomy.feature.ServerFeature;
import com.alazeprt.servereconomy.database.MySQLDatabase;
import com.alazeprt.servereconomy.feature.store.utils.DataUtils;
import com.alazeprt.servereconomy.feature.store.utils.FileUtils;
import com.alazeprt.servereconomy.feature.store.utils.MySQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static com.alazeprt.servereconomy.ServerEconomyPlugin.*;
import static org.bukkit.Bukkit.getServer;

public class ServerStore implements ServerFeature {

    public static ConfigurationSection store_config;

    public static FileConfiguration store_data;

    private final ServerEconomyPlugin plugin;

    public static DataUtils dataUtils;

    public ServerStore(ServerEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        store_config = config.getConfigurationSection("store");
        store_data = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "store.yml"));
        if(mySQLDatabase != null) {
            dataUtils = new MySQLUtils(mySQLDatabase);
        } else {
            dataUtils = new FileUtils();
        }
    }

    @Override
    public void disable() {
        if(dataUtils instanceof MySQLUtils mySQLUtils) {
            mySQLUtils.close();
        }
    }

    public void enableReset() {
        Thread thread = new Thread(() -> {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                data.set("time", data.getInt("time") + 1);
                if(store_data.getBoolean("sell.reset_limit") && (data.getInt("time") / 60.0) % store_data.getInt("sell.reset_time") == 0) {
                    dataUtils.resetSellData();
                    Bukkit.getScheduler().runTask(plugin, () -> getServer().broadcastMessage("§a[ServerStore] 重置物品收购数量!"));
                }
                if(store_data.getBoolean("buy.reset_limit") && (data.getInt("time") / 60.0) % store_data.getInt("buy.reset_time") == 0) {
                    dataUtils.resetBuyData();
                    Bukkit.getScheduler().runTask(plugin, () -> getServer().broadcastMessage("§a[ServerStore] 重置物品购买数量!"));
                }
            }, 0, 1200);
        });
        thread.start();
    }
}
