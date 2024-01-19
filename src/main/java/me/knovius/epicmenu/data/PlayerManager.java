package me.knovius.epicmenu.data;

import lombok.Getter;
import me.knovius.epicmenu.EpicMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private EpicMenu plugin;

    @Getter
    private ConcurrentHashMap<String, PlayerData> playerDataMap;

    public PlayerManager(EpicMenu plugin) {
        this.plugin = plugin;
        playerDataMap = new ConcurrentHashMap<>();
    }

    public void addPlayerData(String uuid, PlayerData playerData) {
        playerDataMap.put(uuid, playerData);
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = playerDataMap.get(player.getUniqueId().toString());
        if (playerData == null) {
            PlayerData playerData1 = new PlayerData(plugin, player);
            addPlayerData(player.getUniqueId().toString(), playerData1);

            return playerData1;
        }
        return playerData;
    }

}
