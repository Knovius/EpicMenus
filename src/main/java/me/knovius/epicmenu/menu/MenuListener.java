package me.knovius.epicmenu.menu;

import me.knovius.epicmenu.EpicMenu;
import me.knovius.epicmenu.data.PlayerData;
import me.knovius.epicmenu.menu.events.MenuClickEvent;
import me.knovius.epicmenu.menu.object.Menu;
import me.knovius.epicmenu.slots.Slot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void invClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = ((Player) event.getWhoClicked()).getPlayer();
        PlayerData playerData = EpicMenu.getInstance().getPlayerManager().getPlayerData(player);
        if (playerData == null) return;
        Menu menu = playerData.getOpenedMenu();
        if (menu == null || menu.getInv() == null) return;
        if (event.getInventory() != menu.getInv()) return;
        //working with the a menu rn
        //which slot b
        event.setCancelled(true);
        Slot slot = menu.getSlot(event.getSlot());
        if (slot == null) return;
        MenuClickEvent menuClickEvent = new MenuClickEvent(player, playerData, menu, slot);
        Bukkit.getPluginManager().callEvent(menuClickEvent);
    }

    @EventHandler
    public void invClose(InventoryCloseEvent event) {
        PlayerData playerData = EpicMenu.getInstance().getPlayerManager().getPlayerData((Player)event.getPlayer());
        if (playerData == null) return;
        if (playerData.getOpenedMenu() == null ||  playerData.getOpenedMenu().getInv() == null) return;
        if (playerData.getOpenedMenu().getInv() == event.getInventory()) {
            playerData.setOpenedMenu(null);
        }
    }

    @EventHandler
    public void invDrag(InventoryDragEvent event) {
        PlayerData playerData = EpicMenu.getInstance().getPlayerManager().getPlayerData((Player)event.getWhoClicked());
        if (playerData == null) return;
        Menu menu = playerData.getOpenedMenu();
        if (playerData.getOpenedMenu() == null ||  playerData.getOpenedMenu().getInv() == null) return;
        if (event.getInventory() != menu.getInv()) return;
        //working with the a menu rn
        //which slot b
        event.setCancelled(true);
    }
}
