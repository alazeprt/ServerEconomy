package com.alazeprt.serverstore.commands.sub;

import com.alazeprt.serverstore.ServerEconomyPlugin;
import com.alazeprt.serverstore.commands.SubCommand;
import com.alazeprt.serverstore.events.StoreEvent;
import com.alazeprt.serverstore.utils.DataUtils;
import com.alazeprt.serverstore.utils.InventoryUtils;
import com.alazeprt.serverstore.utils.StatusUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import static com.alazeprt.serverstore.ServerEconomyPlugin.*;

public class SellCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.sell")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        if(!StatusUtils.sellStatus()) {
            sender.sendMessage("§c目前已停止收购物品! 具体请见/store status");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家才能使用此命令!");
            return;
        }
        Player player = (Player) sender;
        String project = args[1];
        BigDecimal amount = args.length == 3 ? new BigDecimal(args[2]) : new BigDecimal(1);
        if(amount.compareTo(new BigDecimal(1)) < 0 || amount.compareTo(new BigDecimal(amount.intValue())) != 0) {
            sender.sendMessage("§c数量必须为正整数!");
            return;
        }
        if(!store_data.contains("sell." + project)) {
            sender.sendMessage("§c收购项不存在!");
            return;
        }
        if(!DataUtils.sellPerPlayerLimit(project, player.getName(), amount)) {
            sender.sendMessage("§c你收购此物品的数量已达上限!");
            return;
        }
        if(!DataUtils.sellTotalLimit(project, amount)) {
            sender.sendMessage("§c此物品的收购数量已达上限!");
            return;
        }
        BigDecimal sell_amount = store_data.contains("sell." + project + ".amount") ?
                amount.multiply(new BigDecimal(store_data.getString("sell." + project + ".amount"))) : amount;
        BigDecimal money = new BigDecimal(store_data.getString("sell." + project + ".price")).multiply(amount);
        if(!InventoryUtils.hasEnoughItems(player, Material.valueOf(store_data.getString("sell." + project + ".item").toUpperCase()), sell_amount)) {
            sender.sendMessage("§c物品数量不足!");
            return;
        }
        InventoryUtils.removeItems(player, Material.valueOf(store_data.getString("sell." + project + ".item").toUpperCase()), sell_amount);
        economy.depositPlayer(player, money.doubleValue());
        ServerEconomyPlugin.money = ServerEconomyPlugin.money.subtract(money);
        DataUtils.addSellAmount(project, player.getName(), amount);
        player.sendMessage("§a收购成功!");
        eventList.forEach(StoreEvent::onSell);
    }
}
