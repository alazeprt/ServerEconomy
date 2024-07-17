package com.alazeprt.servereconomy;

import com.alazeprt.servereconomy.database.MySQLDatabase;
import com.alazeprt.servereconomy.feature.store.ServerStore;
import com.alazeprt.servereconomy.feature.store.commands.MainCommand;
import com.alazeprt.servereconomy.feature.store.events.ServerStoreEvent;
import com.alazeprt.servereconomy.feature.store.events.StoreEvent;
import com.alazeprt.servereconomy.feature.territory.ServerTerritory;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerEconomyPlugin extends JavaPlugin {

    public static BigDecimal money;

    public static FileConfiguration config;

    public static FileConfiguration data;

    public static GriefPrevention griefPrevention;

    public static Economy economy;

    private ServerStore store;

    private ServerTerritory territory;

    public static MySQLDatabase mySQLDatabase;

    public static final List<StoreEvent> eventList = new ArrayList<>();

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        getLogger().info("Enabling ServerStore");
        getLogger().info("Setting up economy system...");
        if (!setupEconomy()) {
            getLogger().severe("Vault dependency not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(config.getBoolean("database.enable")) {
            getLogger().info("Setting up database...");
            if(config.getString("database.type").equalsIgnoreCase("MySQL")) {
                String host = config.getString("database.mysql.host");
                int port = config.getInt("database.mysql.port");
                String database = config.getString("database.mysql.database");
                String username = config.getString("database.mysql.username");
                String password = config.getString("database.mysql.password");
                String driver = config.getString("database.mysql.driver");
                mySQLDatabase = new MySQLDatabase(host, port, database, username, password);
                mySQLDatabase.initial(driver);
            }
        }
        getLogger().info("Setting up data & configuration file");
        File configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            saveResource("config.yml", false);
        }
        File dataFile = new File(getDataFolder(), "data.yml");
        if(!dataFile.exists()) {
            saveResource("data.yml", false);
        }
        File storeFile = new File(getDataFolder(), "store.yml");
        if(!storeFile.exists()) {
            saveResource("store.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        data = YamlConfiguration.loadConfiguration(dataFile);
        store = new ServerStore(this);
        store.enable();
        if(data.getString("money") == null) {
            data.set("money", new BigDecimal(config.getString("initial")).intValue());
            try {
                data.save(dataFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        money = new BigDecimal(data.getString("money"));
        getLogger().info("Starting thread for data reset");
        store.enableReset();
        if(config.getBoolean("territory.enable")) {
            getLogger().info("Setting up territory system...");
            griefPrevention = GriefPrevention.instance;
            territory = new ServerTerritory(this);
            territory.enable();
        }
        getLogger().info("Setting up command");
        Objects.requireNonNull(getCommand("store")).setExecutor(new MainCommand());
        getLogger().info("Enabling events");
        addEvent(new ServerStoreEvent());
        eventList.forEach(StoreEvent::onEnable);
        getLogger().info("ServerStore is ready! (" + (System.currentTimeMillis() - start) + " ms)");
    }

    @Override
    public void onDisable() {
        long start = System.currentTimeMillis();
        getLogger().info("Disabling ServerEconomy");
        getLogger().info("Disabling features");
        if(territory != null) territory.disable();
        store.disable();
        try {
            // TODO: money, time -> database
            data.set("money", money.doubleValue());
            data.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Disabling events");
        eventList.forEach(StoreEvent::onDisable);
        getLogger().info("ServerStore is disabled! (" + (System.currentTimeMillis() - start) + " ms)");
    }

    public static void addEvent(StoreEvent event) {
        eventList.add(event);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}
