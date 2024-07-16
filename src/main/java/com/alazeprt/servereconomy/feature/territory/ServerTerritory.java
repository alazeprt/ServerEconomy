package com.alazeprt.servereconomy.feature.territory;

import com.alazeprt.servereconomy.ServerEconomyPlugin;
import com.alazeprt.servereconomy.feature.ServerFeature;
import com.alazeprt.servereconomy.feature.territory.utils.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ServerTerritory implements ServerFeature {

    private final ServerEconomyPlugin plugin;

    public static FileConfiguration territory;

    private final List<TerritorySample> sampleList = new ArrayList<>();

    public ServerTerritory(ServerEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        File file = new File(plugin.getDataFolder(), "territory.yml");
        if(!file.exists()) {
            plugin.saveResource("territory.yml", false);
        }
        territory = YamlConfiguration.loadConfiguration(file);
        for(String sectionName : territory.getValues(false).keySet()) {
            ConfigurationSection section = territory.getConfigurationSection(sectionName);
            int min_money = section.getInt("condition.money.min");
            int max_money = section.getInt("condition.money.max");
            Range money = new Range(BigDecimal.valueOf(min_money), BigDecimal.valueOf(max_money));
            int min_territory = section.getInt("condition.territory.min");
            int max_territory = section.getInt("condition.territory.max");
            Range territory = new Range(BigDecimal.valueOf(min_territory), BigDecimal.valueOf(max_territory));
            int min_block = section.getInt("condition.territorial_block.min");
            int max_block = section.getInt("condition.territorial_block.max");
            Range block = new Range(BigDecimal.valueOf(min_block), BigDecimal.valueOf(max_block));
            TerritoryCondition condition = new TerritoryCondition(money, territory, block);
            double initial = section.getDouble("tax.initial");
            double block_magnification = section.getDouble("tax.block_magnification");
            double territory_magnification = section.getDouble("tax.territory_magnification");
            TerritoryTax tax = new TerritoryTax(BigDecimal.valueOf(initial), BigDecimal.valueOf(block_magnification), BigDecimal.valueOf(territory_magnification));
            int interval = section.getInt("interval");
            String timekeeping = section.getString("timekeeping");
            TerritoryTime time = new TerritoryTime(BigDecimal.valueOf(interval), timekeeping);
            TerritorySample sample = new TerritorySample(plugin, condition, tax, time);
            sampleList.add(sample);
            sample.start();
        }
    }

    @Override
    public void disable() {

    }
}
