package com.alazeprt.servereconomy.feature.territory.utils;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import static com.alazeprt.servereconomy.ServerEconomyPlugin.economy;
import static com.alazeprt.servereconomy.ServerEconomyPlugin.griefPrevention;

public class TerritoryCondition {
    private final Range money;

    private final Range block;

    private final Range territory;

    public TerritoryCondition(Range money, Range block, Range territory) {
        this.money = money;
        this.block = block;
        this.territory = territory;
    }

    public boolean check(Player player) {
        if(!money.inRange(BigDecimal.valueOf(economy.getBalance(player)))) return false;
        PlayerData griefPreventionPlayer = griefPrevention.dataStore.getPlayerData(player.getUniqueId());
        if(!territory.inRange(BigDecimal.valueOf(
                griefPreventionPlayer.getClaims().size()
        ))) return false;
        BigDecimal blocks = new BigDecimal(0);
        for(Claim claim : griefPreventionPlayer.getClaims()) {
            blocks = blocks.add(BigDecimal.valueOf(claim.getArea()));
        }
        if(!block.inRange(blocks)) return false;
        return true;
    }

    public boolean check(BigDecimal money, BigDecimal block, BigDecimal territory) {
        if(!this.money.inRange(money)) return false;
        if(!this.block.inRange(block)) return false;
        if(!this.territory.inRange(territory)) return false;
        return true;
    }
}
