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

package com.github.shin_ideal.survivalregionclaimplugin.Listeners;

import com.github.shin_ideal.survivalregionclaimplugin.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {
    @EventHandler
    public void onWand(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        Location location;

        if (!playerData.isWandMode()) {
            return;
        }

        if (!player.getPlayer().getInventory().getItemInHand().getType().equals(Material.GOLD_AXE)) {
            return;
        }

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            location = event.getClickedBlock().getLocation();
            playerData.setPos(1, location);
            player.sendMessage(ChatColor.DARK_PURPLE + "Pos1 Selected.");
            event.setCancelled(true);
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            location = event.getClickedBlock().getLocation();
            playerData.setPos(2, location);
            player.sendMessage(ChatColor.DARK_PURPLE + "Pos2 Selected.");
            event.setCancelled(true);
        }
    }
}
