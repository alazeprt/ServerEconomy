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

public class BuyCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("serverstore.buy")) {
            new NoPermissionCommand().execute(sender, null);
            return;
        }
        if(!StatusUtils.buyStatus()) {
            sender.sendMessage("§c目前已停止购买物品! 具体请见/store status");
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家才能使用此命令!");
            return;
        }
        Player player = (Player) sender;
        String project = args[1];
        BigDecimal amount = args.length == 3 ? new BigDecimal(args[2]) : new BigDecimal(1);
        if(!store_data.contains("buy." + project)) {
            sender.sendMessage("§c购买项不存在!");
            return;
        }
        if(amount.compareTo(new BigDecimal(1)) < 0 || amount.compareTo(new BigDecimal(amount.intValue())) != 0) {
            sender.sendMessage("§c数量必须为正整数!");
            return;
        }
        if(!DataUtils.buyPerPlayerLimit(project, player.getName(), amount)) {
            sender.sendMessage("§c你购买此物品的数量已达上限!");
            return;
        }
        if(!DataUtils.buyTotalLimit(project, amount)) {
            sender.sendMessage("§c此物品的购买数量已达上限!");
            return;
        }
        BigDecimal buy_amount = store_data.contains("buy." + project + ".amount") ?
                amount.multiply(new BigDecimal(store_data.getString("buy." + project + ".amount"))) : amount;
        BigDecimal money = new BigDecimal(store_data.getString("buy." + project + ".price")).multiply(amount);
        if(new BigDecimal(economy.getBalance(player)).compareTo(money) < 0) {
            sender.sendMessage("§c余额不足!");
            return;
        }
        economy.withdrawPlayer(player, money.doubleValue());
        InventoryUtils.giveItemToPlayer(player, Material.valueOf(store_data.getString("buy." + project + ".item").toUpperCase()), buy_amount);
        ServerEconomyPlugin.money = ServerEconomyPlugin.money.add(money);
        player.sendMessage("§a购买成功!");
        DataUtils.addBuyAmount(project, player.getName(), amount);
        eventList.forEach(StoreEvent::onBuy);
    }
}
