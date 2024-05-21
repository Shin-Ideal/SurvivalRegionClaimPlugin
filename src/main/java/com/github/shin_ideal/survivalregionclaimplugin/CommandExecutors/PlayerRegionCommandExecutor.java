/*
 * SurvivalRegionClaimPlugin, Claim Region with diamonds in Minecraft
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 * Copyright (C) WorldGuard team and contributors
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

package com.github.shin_ideal.survivalregionclaimplugin.CommandExecutors;

import com.github.shin_ideal.survivalregionclaimplugin.Functions.PriceCalculatorFunction;
import com.github.shin_ideal.survivalregionclaimplugin.PlayerData;
import com.github.shin_ideal.survivalregionclaimplugin.SurvivalRegionClaimPlugin;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerRegionCommandExecutor implements CommandExecutor {

    private final SurvivalRegionClaimPlugin Instance;
    private final int LOCATIONYMIN;
    private final int LOCATIONYMAX;

    public PlayerRegionCommandExecutor() {
        Instance = SurvivalRegionClaimPlugin.getInstance();
        LOCATIONYMIN = 0;
        LOCATIONYMAX = 255;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Player Only Command.");
            return false;
        }

        Player player = (Player) sender;
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(player.getLocation().getWorld());

        int x, z;

        switch (args.length) {
            case 0:
                sendHowToUseMessage(sender, label);
                break;
            case 1:
                switch (args[0]) {
                    case "togglewand":
                        playerData.setWandMode(!playerData.isWandMode());
                        sender.sendMessage(ChatColor.DARK_PURPLE + "WandMode is " + playerData.isWandMode());
                        if (playerData.isWandMode()) {
                            sender.sendMessage(ChatColor.DARK_GREEN + "Left/Right Click with GoldAxe.");
                        }
                        break;
                    case "list":
                        sender.sendMessage("====================");
                        sender.sendMessage(ChatColor.GOLD + "<" + player.getName() + " 's Regions>");
                        for (String name : regionManager.getRegions().keySet()) {
                            if (regionManager.getRegions().get(name).getOwners().contains(player.getUniqueId())) {
                                sender.sendMessage(name);
                            }
                        }
                        sender.sendMessage("====================");
                        break;
                    case "checkprice":
                        if (playerData.getPos(1) == null || playerData.getPos(2) == null) {
                            sender.sendMessage(ChatColor.RED + "Please Select Position.");
                            return false;
                        }
                        x = Math.abs(playerData.getPos(1).getBlockX() - playerData.getPos(2).getBlockX());
                        z = Math.abs(playerData.getPos(1).getBlockZ() - playerData.getPos(2).getBlockZ());
                        sender.sendMessage(ChatColor.GOLD + "Price:" + ChatColor.GREEN + PriceCalculatorFunction.getPrice(x, z) + ChatColor.GOLD + "diamonds");
                        break;
                    default:
                        sendHowToUseMessage(sender, label);
                        return false;
                }
                break;
            case 2:
                switch (args[0]) {
                    case "claim":
                        if (Instance.getWorldBlackList().contains(player.getWorld().getName())) {
                            sender.sendMessage(ChatColor.RED + "Cannot claim in this world.");
                            return false;
                        }
                        int regionCount = 0;
                        for (String name : regionManager.getRegions().keySet()) {
                            if (regionManager.getRegions().get(name).getOwners().contains(player.getUniqueId())) {
                                regionCount++;
                            }
                        }
                        if (regionCount >= Instance.getRegionLimit()) {
                            sender.sendMessage(ChatColor.RED + "You have many regions.");
                            return false;
                        }
                        int diamondAmount = 0;
                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null && item.getType().equals(Material.DIAMOND)) {
                                diamondAmount += item.getAmount();
                            }
                        }
                        if (playerData.getPos(1) == null || playerData.getPos(2) == null) {
                            sender.sendMessage(ChatColor.RED + "Please Select Position.");
                            return false;
                        }
                        if (regionManager.hasRegion(args[1])) {
                            sender.sendMessage(ChatColor.RED + "Cannot use this name.");
                        }
                        x = Math.abs(playerData.getPos(1).getBlockX() - playerData.getPos(2).getBlockX());
                        z = Math.abs(playerData.getPos(1).getBlockZ() - playerData.getPos(2).getBlockZ());
                        BlockVector pos1BlockVector = new BlockVector(playerData.getPos(1).getBlockX(), LOCATIONYMIN, playerData.getPos(1).getBlockZ());
                        BlockVector pos2BlockVector = new BlockVector(playerData.getPos(2).getBlockX(), LOCATIONYMAX, playerData.getPos(2).getBlockZ());
                        ProtectedRegion protectedRegion = new ProtectedCuboidRegion(args[1], pos1BlockVector, pos2BlockVector);
                        if (protectedRegion.getIntersectingRegions(regionManager.getRegions().values()).size() > 0) {
                            sender.sendMessage(ChatColor.RED + "Intersecting another region.");
                            return false;
                        }
                        int price = PriceCalculatorFunction.getPrice(x, z);
                        if (price > diamondAmount) {
                            sender.sendMessage(ChatColor.RED + "Not enough diamonds.");
                            return false;
                        }
                        player.getInventory().removeItem(new ItemStack(Material.DIAMOND, price));
                        protectedRegion.getOwners().addPlayer(player.getUniqueId());
                        regionManager.addRegion(protectedRegion);
                        sender.sendMessage(ChatColor.DARK_PURPLE + "Claim Region.");
                        break;
                    case "remove":
                        if (!regionManager.hasRegion(args[1]) || !regionManager.getRegion(args[1]).getOwners().contains(player.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "Not Found Region.");
                            return false;
                        }
                        x = Math.abs(regionManager.getRegion(args[1]).getMinimumPoint().getBlockX() - regionManager.getRegion(args[1]).getMaximumPoint().getBlockX());
                        z = Math.abs(regionManager.getRegion(args[1]).getMinimumPoint().getBlockZ() - regionManager.getRegion(args[1]).getMaximumPoint().getBlockZ());
                        int returnAmount = PriceCalculatorFunction.getMultiplierPrice(x, z);
                        if (returnAmount > 0) {
                            player.getInventory().addItem(new ItemStack(Material.DIAMOND, returnAmount));
                        }
                        regionManager.removeRegion(args[1]);
                        sender.sendMessage(ChatColor.DARK_PURPLE + "Remove Region.");
                        break;
                    case "memberlist":
                        if (!regionManager.hasRegion(args[1]) || !regionManager.getRegion(args[1]).getOwners().contains(player.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "Not Found Region.");
                            return false;
                        }
                        sender.sendMessage("====================");
                        sender.sendMessage(ChatColor.GOLD + "<" + args[1] + " 's members>");
                        for (String name : regionManager.getRegion(args[1]).getMembers().getPlayers()) {
                            sender.sendMessage(name);
                        }
                        sender.sendMessage("====================");
                        break;
                    default:
                        sendHowToUseMessage(sender, label);
                        return false;
                }
                break;
            case 3:
                switch (args[0]) {
                    case "addmember":
                        if (!regionManager.hasRegion(args[1]) || !regionManager.getRegion(args[1]).getOwners().contains(player.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "Not Found Region.");
                            return false;
                        }
                        if (regionManager.getRegion(args[1]).getMembers().getPlayers().contains(args[2].toLowerCase())) {
                            sender.sendMessage(ChatColor.RED + "Player already Member.");
                            return false;
                        }
                        regionManager.getRegion(args[1]).getMembers().addPlayer(args[2]);
                        sender.sendMessage(ChatColor.DARK_PURPLE + "Add Member.");
                        break;
                    case "removemember":
                        if (!regionManager.hasRegion(args[1]) || !regionManager.getRegion(args[1]).getOwners().contains(player.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "Not Found Region.");
                            return false;
                        }
                        if (!regionManager.getRegion(args[1]).getMembers().getPlayers().contains(args[2].toLowerCase())) {
                            sender.sendMessage(ChatColor.RED + "Member not found.");
                            return false;
                        }
                        regionManager.getRegion(args[1]).getMembers().removePlayer(args[2]);
                        sender.sendMessage(ChatColor.DARK_PURPLE + "Remove Member.");
                        break;
                    default:
                        sendHowToUseMessage(sender, label);
                        return false;
                }
                break;
            default:
                sendHowToUseMessage(sender, label);
                return false;
        }
        return true;
    }

    private void sendHowToUseMessage(CommandSender sender, String label) {
        sender.sendMessage("/" + label + " togglewand");
        sender.sendMessage("/" + label + " claim <id>");
        sender.sendMessage("/" + label + " remove <id>");
        sender.sendMessage("/" + label + " list");
        sender.sendMessage("/" + label + " checkprice");
        sender.sendMessage("/" + label + " memberlist <id>");
        sender.sendMessage("/" + label + " addmember <id> <player>");
        sender.sendMessage("/" + label + " removemember <id> <player>");
    }
}
