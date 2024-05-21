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

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private static final List<PlayerData> playerDataList = new ArrayList<>();

    public static PlayerData createPlayerData(UUID uuid) {
        PlayerData playerData = new PlayerData(uuid);
        playerDataList.add(playerData);
        return playerData;
    }

    public static PlayerData getPlayerData(UUID uuid) {
        for (PlayerData playerData : playerDataList) {
            if (playerData.getUuid().equals(uuid)) {
                return playerData;
            }
        }
        return createPlayerData(uuid);
    }

    private final UUID uuid;
    private boolean wandMode;
    private Location pos1;
    private Location pos2;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        wandMode = false;
        pos1 = null;
        pos2 = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isWandMode() {
        return wandMode;
    }

    public void setWandMode(boolean wandMode) {
        this.wandMode = wandMode;
    }

    public Location getPos(int i) {
        switch (i) {
            case 1:
                return pos1;
            case 2:
                return pos2;
            default:
                return null;
        }
    }

    public void setPos(int i, Location location) {
        switch (i) {
            case 1:
                pos1 = location;
                break;
            case 2:
                pos2 = location;
                break;
            default:
                break;
        }
    }
}
