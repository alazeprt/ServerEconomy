package com.alazeprt.serverstore.commands.sub;

import com.alazeprt.serverstore.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.help")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        sender.sendMessage("§aServerStore 帮助");
        sender.sendMessage("§c/store 或 /store help   §e- 查看帮助文档");
        sender.sendMessage("§c/store list [buy/sell]  §e- 查看商店所有物品(可指定只看出售/收购)");
        sender.sendMessage("§c/store sell <名称> [数量]  §e- 收购你的指定物品");
        sender.sendMessage("§c/store buy <名称> [数量]  §e- 出售你的指定物品");
        sender.sendMessage("§c/store status   §e- 查看商店状态");
        sender.sendMessage("§c/store search <关键词>  §e- 搜索商店物品");
    }
}
