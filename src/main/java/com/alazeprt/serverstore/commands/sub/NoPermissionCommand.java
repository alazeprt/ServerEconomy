package com.alazeprt.serverstore.commands.sub;

import com.alazeprt.serverstore.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class NoPermissionCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§c你没有权限执行该指令!");
    }
}
