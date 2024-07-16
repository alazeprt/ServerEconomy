package com.alazeprt.servereconomy.feature.territory.utils;

import com.alazeprt.servereconomy.ServerEconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TerritoryTime {
    private final BigDecimal interval;

    private final TimekeepingMethod method;

    private final Map<String, BigDecimal> time = new HashMap<>();

    public TerritoryTime(BigDecimal interval, TimekeepingMethod method) {
        this.interval = interval;
        this.method = method;
    }

    public TerritoryTime(BigDecimal interval, String method) {
        this.interval = interval;
        this.method = TimekeepingMethod.valueOf(method.toUpperCase());
    }

    public void start(ServerEconomyPlugin plugin, Runnable task) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if(method == TimekeepingMethod.SYSTEM_TIME) {
                for(Map.Entry<String, BigDecimal> entry : time.entrySet()) {
                    time.put(entry.getKey(), entry.getValue().add(BigDecimal.ONE));
                }
            } else if(method == TimekeepingMethod.ONLINE_TIME) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(!time.containsKey(player.getName())) {
                        time.put(player.getName(), BigDecimal.ONE);
                    }
                    time.put(player.getName(), time.get(player.getName()).add(BigDecimal.ONE));
                }
            }
            task.run();
        }, 0, 1200);
    }

    public Map<String, BigDecimal> getTime() {
        return time;
    }
}
