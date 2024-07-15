package com.alazeprt.serverstore.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;

public class InventoryUtils {
    public static boolean hasEnoughItems(Player player, Material material, BigDecimal amount) {
        PlayerInventory inventory = player.getInventory();
        BigDecimal found = BigDecimal.ZERO;

        // 遍历背包中的每个物品
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                found = found.add(BigDecimal.valueOf(item.getAmount())); // 累加找到的物品数量
            }
            if (found.compareTo(amount) >= 0) {
                return true; // 如果找到了足够的物品，返回true
            }
        }

        // 检查背包中的装备（盔甲和手持物品）
        ItemStack[] armorContents = inventory.getArmorContents();
        for (ItemStack item : armorContents) {
            if (item != null && item.getType() == material) {
                found = found.add(BigDecimal.valueOf(item.getAmount()));
            }
            if (found.compareTo(amount) >= 0) {
                return true;
            }
        }

        ItemStack offHandItem = inventory.getItemInOffHand();
        if (offHandItem != null && offHandItem.getType() == material) {
            found = found.add(BigDecimal.valueOf(offHandItem.getAmount()));
        }

        return found.compareTo(amount) >= 0; // 如果总数达到或超过所需数量，返回true
    }

    public static boolean removeItems(Player player, Material material, BigDecimal amount) {
        PlayerInventory inventory = player.getInventory();
        BigDecimal toRemove = amount;

        // 遍历背包中的每个物品
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                BigDecimal itemAmount = BigDecimal.valueOf(item.getAmount());
                if (itemAmount.compareTo(toRemove) <= 0) {
                    toRemove = toRemove.subtract(itemAmount);
                    inventory.removeItem(item); // 完全移除这个物品堆
                } else {
                    int newAmount = itemAmount.subtract(toRemove).intValue();
                    item.setAmount(newAmount); // 减少物品数量
                    toRemove = BigDecimal.ZERO;
                }
                if (toRemove.compareTo(BigDecimal.ZERO) <= 0) {
                    player.updateInventory(); // 更新背包以反映变化
                    return true; // 如果移除了足够的物品，返回true
                }
            }
        }

        // 如果还需要移除物品，检查背包中的装备（盔甲和手持物品）
        ItemStack[] armorContents = inventory.getArmorContents();
        for (ItemStack item : armorContents) {
            if (item != null && item.getType() == material) {
                BigDecimal itemAmount = BigDecimal.valueOf(item.getAmount());
                if (itemAmount.compareTo(toRemove) <= 0) {
                    toRemove = toRemove.subtract(itemAmount);
                    inventory.removeItem(item);
                } else {
                    int newAmount = itemAmount.subtract(toRemove).intValue();
                    item.setAmount(newAmount);
                    toRemove = BigDecimal.ZERO;
                }
                if (toRemove.compareTo(BigDecimal.ZERO) <= 0) {
                    player.updateInventory();
                    return true;
                }
            }
        }

        ItemStack offHandItem = inventory.getItemInOffHand();
        if (offHandItem != null && offHandItem.getType() == material) {
            BigDecimal itemAmount = BigDecimal.valueOf(offHandItem.getAmount());
            if (itemAmount.compareTo(toRemove) <= 0) {
                toRemove = toRemove.subtract(itemAmount);
                inventory.removeItem(offHandItem);
            } else {
                int newAmount = itemAmount.subtract(toRemove).intValue();
                offHandItem.setAmount(newAmount);
                toRemove = BigDecimal.ZERO;
            }
        }

        if (toRemove.compareTo(BigDecimal.ZERO) <= 0) {
            player.updateInventory();
            return true;
        }

        return false; // 如果没有足够的物品，返回false
    }

    public static void giveItemToPlayer(Player player, Material material, BigDecimal amount) {
        // 创建一个具有指定数量和类型的 ItemStack
        ItemStack item = new ItemStack(material, amount.intValue());

        // 检查玩家背包是否有足够的空间
        if (player.getInventory().firstEmpty() != -1) {
            // 如果有空间，直接给予玩家物品
            player.getInventory().addItem(item);
        } else {
            // 如果没有空间，将物品掉落在玩家脚下
            player.getWorld().dropItem(player.getLocation(), item);
        }
    }
}
