package com.alazeprt.serverstore.commands;

import com.alazeprt.serverstore.commands.sub.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        switch (strings.length) {
            case 0:
                new HelpCommand().execute(commandSender, strings);
                break;
            case 1:
                if(strings[0].equalsIgnoreCase("help")) {
                    new HelpCommand().execute(commandSender, strings);
                    break;
                } else if(strings[0].equalsIgnoreCase("status")) {
                    new StatusCommand().execute(commandSender, strings);
                    break;
                } else if(strings[0].equalsIgnoreCase("list")) {
                    new ListCommand().execute(commandSender, strings);
                    break;
                }
            case 2:
                if(strings[0].equalsIgnoreCase("list")) {
                    new ListCommand().execute(commandSender, strings);
                    break;
                }
                if(strings[0].equalsIgnoreCase("buy")) {
                    new BuyCommand().execute(commandSender, strings);
                    break;
                }
                if(strings[0].equalsIgnoreCase("sell")) {
                    new SellCommand().execute(commandSender, strings);
                    break;
                }
                if(strings[0].equalsIgnoreCase("search")) {
                    new SearchCommand().execute(commandSender, strings);
                    break;
                }
            case 3:
                if(strings[0].equalsIgnoreCase("buy")) {
                    new BuyCommand().execute(commandSender, strings);
                    break;
                }
                if(strings[0].equalsIgnoreCase("sell")) {
                    new SellCommand().execute(commandSender, strings);
                    break;
                }
            default:
                new ErrorCommand().execute(commandSender, strings);
        }
        return false;
    }
}
