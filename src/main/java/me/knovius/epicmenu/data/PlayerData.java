package me.knovius.epicmenu.data;

import lombok.Getter;
import lombok.Setter;
import me.knovius.epicmenu.EpicMenu;
import me.knovius.epicmenu.menu.object.Menu;
import org.bukkit.entity.Player;

public class PlayerData {

    private EpicMenu plugin;

    @Getter
    private Player player;
    @Getter
    @Setter
    private Menu openedMenu;
    public PlayerData(EpicMenu plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
}
