package me.knovius.epicmenu.slots;

import lombok.Getter;
import me.knovius.epicmenu.EpicMenu;
import me.knovius.epicmenu.utils.CM;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public class Slot {

    private int slotId;
    private String name;
    private Material material;
    private int customModelData;
    private List<String> lore;
    private boolean closeInventory;

    //Part of a frame
    private boolean isFrame;
    private List<String> byPlayerCommands;
    private List<String> byConsoleCommands;

    private List<String> messages;
    private String permission;

    public Slot(ConfigurationSection path, int slotId, boolean isFrame) {
        this.slotId = slotId;

        name = path.getString("item.name");
        material = Material.valueOf(path.getString("item.material").toUpperCase());
        lore = path.getStringList("item.lore");

        customModelData = path.getInt("item.customModelData");
        closeInventory = path.getBoolean("actions.closeInventory");
        this.isFrame = isFrame;

        byPlayerCommands = path.getStringList("actions.commands.byPlayer");
        byConsoleCommands = path.getStringList("actions.commands.byConsole");
        messages = path.getStringList("actions.sendMessage");
        permission = path.getString("actions.permission");

    }

    public ItemStack toItem() {

        if (material == null) return null;

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CM.color(name));
        itemMeta.setLore(CM.colorLore(lore));
        itemMeta.setCustomModelData(customModelData);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void execute(Player player) {

        if (permission != null && !player.hasPermission(permission)) {

            return;
        }
        for (String s : messages) {
            player.sendMessage(CM.color(s.replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix")).replaceAll("%player%", player.getName())));
        }


        for (String s: byConsoleCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("%player%", player.getName()));
        }
        for (String s: byPlayerCommands) {
            player.performCommand(s.replaceAll("%player%", player.getName()));
        }
        if (closeInventory) {
            player.closeInventory();
        }

    }
}
