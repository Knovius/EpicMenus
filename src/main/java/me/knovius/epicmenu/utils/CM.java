package me.knovius.epicmenu.utils;

import me.knovius.epicmenu.EpicMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CM {

    public static String color(String s) {
       return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static List<String> colorLore(List<String> string) {

        List<String> coloredLore = new ArrayList<>();

        if (string.size() < 1) {
            return null;
        }

        for (int x = 0; x < string.size(); x++) {
            coloredLore.add(color(string.get(x)));
        }
        return coloredLore;
    }

    public static void sendHelpMessage(Player player) {
        List<String> lore = EpicMenu.getInstance().getConfig().getStringList("messages.help");
        for (int x = 0; x<lore.size(); x++) {
            player.sendMessage(CM.color(lore.get(x).replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix"))));
        }
    }
}
