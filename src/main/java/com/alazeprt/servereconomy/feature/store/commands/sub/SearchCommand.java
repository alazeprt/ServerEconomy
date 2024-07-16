package com.alazeprt.servereconomy.feature.store.commands.sub;

import com.alazeprt.servereconomy.feature.store.commands.SubCommand;
import com.alazeprt.servereconomy.utils.TimeUtils;
import org.bukkit.command.CommandSender;

import static com.alazeprt.servereconomy.feature.store.ServerStore.store_data;

public class SearchCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.search")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        sender.sendMessage("§9收购物品搜索结果:");
        for(String key : store_data.getConfigurationSection("sell").getKeys(false)) {
            if(key.equalsIgnoreCase("player_limit") || key.equalsIgnoreCase("total_limit")
                    || key.equalsIgnoreCase("reset_time") || key.equalsIgnoreCase("reset_limit")) {
                continue;
            }
            if(!key.contains(args[1]) && !store_data.getString("sell." + key + ".item").contains(args[1])) {
                continue;
            }
            sender.sendMessage("§9" + key + ": ");
            sender.sendMessage("  §7物品: " + store_data.getString("sell." + key + ".item"));
            sender.sendMessage("  §7数量: " + store_data.getString("sell." + key + ".amount"));
            sender.sendMessage("  §7价格: " + store_data.getString("sell." + key + ".price"));
            if(store_data.getBoolean("sell.player_limit")) {
                sender.sendMessage("  §7单个玩家限购(" + TimeUtils.formatTime(store_data.getLong("sell.reset_time")) +
                        "): " + store_data.getString("sell." + key + ".player_limit"));
            }
            if(store_data.getBoolean("sell.total_limit")) {
                sender.sendMessage("  §7服务器总限购(" + TimeUtils.formatTime(store_data.getLong("sell.reset_time")) +
                        "): " + store_data.getString("sell." + key + ".total_limit"));
            }
        }
        sender.sendMessage("§c购买物品搜索结果:");
        for(String key : store_data.getConfigurationSection("buy").getKeys(false)) {
            if(key.equalsIgnoreCase("player_limit") || key.equalsIgnoreCase("total_limit")
                    || key.equalsIgnoreCase("reset_time") || key.equalsIgnoreCase("reset_limit")) {
                continue;
            }
            if(!key.contains(args[1]) && !store_data.getString("buy." + key + ".item").contains(args[1])) {
                continue;
            }
            sender.sendMessage("§c" + key + ": ");
            sender.sendMessage("  §7物品: " + store_data.getString("buy." + key + ".item"));
            sender.sendMessage("  §7数量: " + store_data.getString("buy." + key + ".amount"));
            sender.sendMessage("  §7价格: " + store_data.getString("buy." + key + ".price"));
            if(store_data.getBoolean("buy.player_limit")) {
                sender.sendMessage("  §7单个玩家限购(" + TimeUtils.formatTime(store_data.getLong("buy.reset_time")) +
                        "): " + store_data.getString("buy." + key + ".player_limit"));
            }
            if(store_data.getBoolean("buy.total_limit")) {
                sender.sendMessage("  §7服务器总限购(" + TimeUtils.formatTime(store_data.getLong("buy.reset_time")) +
                        "): " + store_data.getString("buy." + key + ".total_limit"));
            }
        }
    }
}
