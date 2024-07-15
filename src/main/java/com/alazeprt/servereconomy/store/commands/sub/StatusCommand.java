package com.alazeprt.servereconomy.store.commands.sub;

import com.alazeprt.servereconomy.store.commands.SubCommand;
import com.alazeprt.servereconomy.store.utils.StatusUtils;
import org.bukkit.command.CommandSender;

import static com.alazeprt.servereconomy.ServerEconomyPlugin.money;
import static com.alazeprt.servereconomy.store.ServerStore.store_config;

public class StatusCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.status")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        sender.sendMessage("§aServerStore 状态");
        sender.sendMessage("§e收购商店: " + (StatusUtils.sellStatus() ? "§a开启" : "§c关闭"));
        sender.sendMessage("§e出售商店: " + (StatusUtils.buyStatus() ? "§a开启" : "§c关闭"));
        if(store_config.getBoolean("status.money")) {
            sender.sendMessage("§e商店存款: §6" + money);
        }
        if(store_config.getBoolean("status.stop")) {
            if(store_config.getBoolean("stop.enable")) {
                sender.sendMessage("§e停止收购/出售所需的存款: §6" + store_config.getString("stop.money"));
            } else {
                sender.sendMessage("§e停止收购/出售所需的存款: §c永久不会停止");
            }
        }
        if(store_config.getBoolean("status.restore")) {
            if(store_config.getBoolean("restore.enable")) {
                sender.sendMessage("§e恢复收购/出售所需的存款: §6" + store_config.getString("restore.money"));
            } else {
                sender.sendMessage("§e恢复收购/出售所需的存款: §c永久不会恢复");
            }
        }
    }
}
