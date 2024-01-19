package me.knovius.epicmenu.listeners;

import me.knovius.epicmenu.menu.events.MenuClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryListener implements Listener {

    @EventHandler
    public void menuClick(MenuClickEvent event) {
        event.getSlot().execute(event.getPlayer());
    }
}
