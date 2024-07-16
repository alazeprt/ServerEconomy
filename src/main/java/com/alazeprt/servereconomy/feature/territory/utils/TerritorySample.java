package com.alazeprt.servereconomy.feature.territory.utils;

import com.alazeprt.servereconomy.ServerEconomyPlugin;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.*;

import static com.alazeprt.servereconomy.ServerEconomyPlugin.economy;
import static com.alazeprt.servereconomy.ServerEconomyPlugin.griefPrevention;

public class TerritorySample {
    private final ServerEconomyPlugin plugin;

    private final TerritoryCondition condition;

    private final TerritoryTax tax;

    private final TerritoryTime time;

    private final Map<Player, BigDecimal> blackList = new HashMap<>();

    public TerritorySample(ServerEconomyPlugin plugin, TerritoryCondition condition, TerritoryTax tax, TerritoryTime time) {
        this.plugin = plugin;
        this.condition = condition;
        this.tax = tax;
        this.time = time;
    }

    public void start() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            time.start(plugin, () -> {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(!condition.check(player)) continue;
                    if(time.getTime().get(player.getName()).remainder(time.getInterval()).compareTo(BigDecimal.ZERO) != 0) continue;
                    BigDecimal money = tax.calculate(player);
                    if(BigDecimal.valueOf(economy.getBalance(player)).compareTo(money) > 0) {
                        if(!blackList.containsKey(player)) {
                            economy.withdrawPlayer(player.getName(), money.doubleValue());
                            player.sendMessage(ChatColor.GREEN + "已扣除领地税 " +
                                    ChatColor.GOLD + money + ChatColor.GREEN + " 金币!");
                            ServerEconomyPlugin.money = ServerEconomyPlugin.money.add(money);
                        } else {
                            if(BigDecimal.valueOf(economy.getBalance(player)).subtract(blackList.get(player)).compareTo(money) > 0) {
                                economy.withdrawPlayer(player.getName(), money.doubleValue());
                                player.sendMessage(ChatColor.GREEN + "已扣除领地税 " +
                                        ChatColor.GOLD + money + ChatColor.GREEN + " 金币!");
                                blackList.remove(player);
                                ServerEconomyPlugin.money = ServerEconomyPlugin.money.add(money);
                            } else {
                                economy.withdrawPlayer(player.getName(), money.doubleValue());
                                player.sendMessage(ChatColor.GREEN + "已扣除本次领地税 " +
                                        ChatColor.GOLD + money + ChatColor.GREEN + " 金币!"
                                        + ChatColor.RED + "\n你还需要缴纳剩余领地税" +
                                        blackList.get(player) + "!");
                                ServerEconomyPlugin.money = ServerEconomyPlugin.money.add(money);
                            }
                        }
                    } else {
                        if(!blackList.containsKey(player)) {
                            blackList.put(player, money);
                            player.sendMessage(ChatColor.RED + "你没有足够的钱缴纳领地税!" +
                                    ChatColor.GOLD + "如有再犯, 则会删除你当前最小的领地!");
                        } else {
                            blackList.put(player, money.add(blackList.get(player)));
                            removeSmallest(player);
                            player.sendMessage(ChatColor.RED + "你没有足够的钱缴纳领地税! 已删除最小领地!" +
                                    ChatColor.GOLD + "如有再犯, 则会删除你下次最小的领地!");
                        }
                    }
                }
            });
        });
    }

    private void removeSmallest(Player player) {
        Vector<Claim> vector = griefPrevention.dataStore.getPlayerData(player.getUniqueId()).getClaims();
        Claim smallest = vector.get(0);
        for(Claim claim : vector) {
            if(claim.getArea() < smallest.getArea()) smallest = claim;
        }
        griefPrevention.dataStore.deleteClaim(smallest);
    }
}
