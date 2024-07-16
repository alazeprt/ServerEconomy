package com.alazeprt.servereconomy.feature.territory.utils;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Vector;

import static com.alazeprt.servereconomy.ServerEconomyPlugin.griefPrevention;

public class TerritoryTax {
    private final BigDecimal initial;

    private final BigDecimal block_magnification;

    private final BigDecimal territory_magnification;

    public TerritoryTax(BigDecimal initial, BigDecimal block_magnification, BigDecimal territory_magnification) {
        this.initial = initial;
        this.block_magnification = block_magnification;
        this.territory_magnification = territory_magnification;
    }

    public BigDecimal calculate(BigDecimal block, BigDecimal territory) {
        return initial.add(block.multiply(block_magnification)).add(territory.multiply(territory_magnification));
    }

    public BigDecimal calculate(Player player) {
        PlayerData griefPreventionPlayer = griefPrevention.dataStore.getPlayerData(player.getUniqueId());
        BigDecimal result = new BigDecimal(String.valueOf(initial));
        Vector<Claim> claims = griefPreventionPlayer.getClaims();
        result = result.add(territory_magnification.multiply(
                BigDecimal.valueOf(claims.size())));
        BigDecimal blocks = new BigDecimal(0);
        for (Claim claim : claims) {
            blocks = blocks.add(BigDecimal.valueOf(claim.getArea()));
        }
        result = result.add(block_magnification.multiply(blocks));
        return result;
    }
}
