package me.knovius.epicmenu.menu.events;

import lombok.Getter;
import me.knovius.epicmenu.data.PlayerData;
import me.knovius.epicmenu.menu.object.Menu;
import me.knovius.epicmenu.slots.Slot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MenuClickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private Player player;

    @Getter
    private PlayerData playerData;

    @Getter
    private Menu menu;

    @Getter
    private Slot slot;


    public MenuClickEvent(Player player, PlayerData playerData, Menu menu, Slot slot) {
        this.player = player;
        this.playerData = playerData;
        this.menu = menu;
        this.slot = slot;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
