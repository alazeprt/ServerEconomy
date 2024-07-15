package com.alazeprt.servereconomy.store.commands.sub;

import com.alazeprt.servereconomy.store.commands.SubCommand;
import com.alazeprt.servereconomy.utils.TimeUtils;
import org.bukkit.command.CommandSender;

import static com.alazeprt.servereconomy.store.ServerStore.store_data;

public class ListCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.list")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        boolean sellList = false;
        boolean buyList = false;
        if(args.length > 1) {
            if(args[1].equals("buy")) {
                buyList = true;
            } else if(args[1].equals("sell")) {
                sellList = true;
            } else {
                new ErrorCommand().execute(sender, args);
            }
        } else {
            sellList = true;
            buyList = true;
        }
        if(sellList) {
            sendSellList(sender);
        }
        if(buyList) {
            sendBuyList(sender);
        }
    }

    private void sendSellList(CommandSender sender) {
        sender.sendMessage("§9收购物品列表:");
        for(String key : store_data.getConfigurationSection("sell").getKeys(false)) {
            if(key.equalsIgnoreCase("player_limit") || key.equalsIgnoreCase("total_limit") ||
                    key.equalsIgnoreCase("reset_time") || key.equalsIgnoreCase("reset_limit")) {
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
    }

    private void sendBuyList(CommandSender sender) {
        sender.sendMessage("§c购买物品列表:");
        for(String key : store_data.getConfigurationSection("buy").getKeys(false)) {
            if(key.equalsIgnoreCase("player_limit") || key.equalsIgnoreCase("total_limit")
                    || key.equalsIgnoreCase("reset_time") || key.equalsIgnoreCase("reset_limit")) {
                continue;
            }
            sender.sendMessage("§c" + key + ": ");
            sender.sendMessage("  §7物品: " + store_data.getString("buy." + key + ".item"));
            sender.sendMessage("  §7数量: " + store_data.getString("buy." + key + ".amount"));
            sender.sendMessage("  §7价格: " + store_data.getString("buy." + key + ".price"));
            if(store_data.getBoolean("buy.player_limit")) {
                sender.sendMessage("  §7单个玩家限购(" + TimeUtils.formatTime(store_data.getLong("buy.reset_time")) + "): " + store_data.getString("buy." + key + ".player_limit"));
            }
            if(store_data.getBoolean("buy.total_limit")) {
                sender.sendMessage("  §7服务器总限购(" + TimeUtils.formatTime(store_data.getLong("buy.reset_time")) + "): " + store_data.getString("buy." + key + ".total_limit"));
            }
        }
    }
}
