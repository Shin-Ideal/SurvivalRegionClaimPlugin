/*
 * SurvivalRegionClaimPlugin, Claim Region with diamonds in Minecraft
 * Copyright (C) 2024  Shin-Ideal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.shin_ideal.survivalregionclaimplugin;

import com.github.shin_ideal.survivalregionclaimplugin.CommandExecutors.PlayerRegionCommandExecutor;
import com.github.shin_ideal.survivalregionclaimplugin.Listeners.WandListener;
import com.github.shin_ideal.survivalregionclaimplugin.TabCompleters.PlayerRegionTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SurvivalRegionClaimPlugin extends JavaPlugin {

    private static SurvivalRegionClaimPlugin Instance;

    private double blockPrice;
    private int regionLimit;
    private double removeReturnPriceMultiplier;
    private List<String> worldBlackList;

    public static SurvivalRegionClaimPlugin getInstance() {
        return Instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enable");
        Instance = this;
        loadConfigData();
        registerListeners();
        registerCommandExecutors();
        registerTabCompleter();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disable");
    }

    private void loadConfigData() {
        saveDefaultConfig();
        blockPrice = getConfig().getDouble("blockprice", 0.0625);
        regionLimit = getConfig().getInt("regionlimit", 0);
        removeReturnPriceMultiplier = getConfig().getDouble("removereturnpricemultiplier", 0.0);
        worldBlackList = getConfig().getStringList("worldblacklist");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new WandListener(), this);
    }

    private void registerCommandExecutors() {
        getCommand("playerregion").setExecutor(new PlayerRegionCommandExecutor());
    }

    private void registerTabCompleter() {
        getCommand("playerregion").setTabCompleter(new PlayerRegionTabCompleter());
    }

    public double getBlockPrice() {
        return blockPrice;
    }

    public int getRegionLimit() {
        return regionLimit;
    }

    public double getRemoveReturnPriceMultiplier() {
        return removeReturnPriceMultiplier;
    }

    public List<String> getWorldBlackList() {
        return worldBlackList;
    }
}
