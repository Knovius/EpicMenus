package me.knovius.epicmenu.commands;

import me.knovius.epicmenu.EpicMenu;
import me.knovius.epicmenu.data.PlayerData;
import me.knovius.epicmenu.menu.MenuManager;
import me.knovius.epicmenu.menu.object.Menu;
import me.knovius.epicmenu.utils.CM;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("epicmenu")) {

            if(!(sender instanceof Player)) {
                sender.sendMessage("can't run this command if not player!");
                return true;
            }
            Player player = (Player) sender;
            ConfigurationSection messages = EpicMenu.getInstance().getConfig().getConfigurationSection("messages");
            String invalidMenu = CM.color(messages.getString("invalid-menu").replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix")));
            String noPermission = CM.color(messages.getString("no-permission").replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix")));

            if (args.length < 1) {
                //change this to help
                CM.sendHelpMessage(player);
                return true;
            }
            if (args[0].length() < 1) {
                player.sendMessage(invalidMenu);
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (!player.hasPermission("epicmenu.list")) {
                    player.sendMessage(noPermission);
                    return true;
                }
                player.sendMessage(CM.color(EpicMenu.getInstance().getConfig().getString("prefix") + " &7Current Menus: "));

                for (File s: EpicMenu.getInstance().getMenuManager().getDirectory().listFiles()) {
                    player.sendMessage(CM.color("&8-&f " + s.getName().replaceAll(".yml", "")));
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {

                if (!player.hasPermission("epicmenu.admin")) {
                    player.sendMessage(noPermission);
                    return true;
                }

                if (args.length < 2 || args[1].length() < 1) {
                    CM.sendHelpMessage(player);
                    return true;
                }
                String reloaded = CM.color(messages.getString("reloaded").replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix")));
                if (args[1].equalsIgnoreCase("all")) {
                    EpicMenu.getInstance().getMenuManager().reloadAllMenu();
                    player.sendMessage(reloaded);
                    return true;
                }
                if (args[1].equalsIgnoreCase("config")) {
                    EpicMenu.getInstance().reloadConfig();
                    player.sendMessage(reloaded);
                    return true;
                }
                if (!EpicMenu.getInstance().getMenuManager().isMenu(args[1]+ ".yml")) {
                    player.sendMessage(invalidMenu);
                    return true;
                }
                EpicMenu.getInstance().getMenuManager().reloadMenu(args[1]+ ".yml");
                player.sendMessage(reloaded);
                return true;
            }
            if (args[0].equalsIgnoreCase("create")) {

                if (!player.hasPermission("epicmenu.admin")) {
                    player.sendMessage(noPermission);
                    return true;
                }

                if (args.length < 2 || args[1].length() < 1) {
                    CM.sendHelpMessage(player);
                    return true;
                }
                EpicMenu.getInstance().getMenuManager().createMenu(args[1]);
                String createdMenu = CM.color(messages.getString("created-menu").replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix")));
                player.sendMessage(createdMenu);

                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {

                if (!player.hasPermission("epicmenu.admin")) {
                    player.sendMessage(noPermission);
                    return true;
                }

                if (args.length < 2 || args[1].length() < 1) {
                    CM.sendHelpMessage(player);
                    return true;
                }
                if (!EpicMenu.getInstance().getMenuManager().isMenu(args[1]+ ".yml")) {
                    player.sendMessage(invalidMenu);
                    return true;
                }
                EpicMenu.getInstance().getMenuManager().removeMenu(args[1]);
                String removedMenu = CM.color(messages.getString("removed-menu").replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix")));
                player.sendMessage(removedMenu);
                return true;
            }


            if (!EpicMenu.getInstance().getMenuManager().isMenu(args[0] + ".yml")) {
                player.sendMessage(invalidMenu);
                return true;
            }
            //working with a real menu sick
            Menu menu = EpicMenu.getInstance().getMenuManager().getMenuMap().get(args[0]+ ".yml");
            if (menu == null) return true;
            if (menu.getPermission() != null) {
                if (!player.hasPermission(menu.getPermission())) {
                    player.sendMessage(CM.color(messages.getString("no-permission").replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix"))));
                    return true;
                }
            }
            PlayerData playerData = EpicMenu.getInstance().getPlayerManager().getPlayerData(player);
            if (playerData == null || menu == null) return true;

            menu.open(player);
            playerData.setOpenedMenu(menu);
            player.sendMessage(CM.color(messages.getString("opening-menu").replaceAll("%prefix%", EpicMenu.getInstance().getConfig().getString("prefix")).replaceAll("%menu%", args[0])));
            return true;
        }
        return false;
    }
}
