package com.alazeprt.servereconomy.store.commands.sub;

import com.alazeprt.servereconomy.store.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class ErrorCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§c错误的命令用法! 使用/store查看帮助文档!");
    }
}
