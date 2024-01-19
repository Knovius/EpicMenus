package me.knovius.epicmenu.menu;

import lombok.Getter;
import me.knovius.epicmenu.EpicMenu;
import me.knovius.epicmenu.data.PlayerData;
import me.knovius.epicmenu.menu.object.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class MenuManager {

    private final EpicMenu plugin;

    private File directory;

    private HashMap<String, File> menuFileMap;
    private HashMap<String, Configuration> menuConfigMap;
    private List<String> menuIds;

    private HashMap<String, Menu> menuMap;

    public MenuManager(EpicMenu plugin) {
        this.plugin = plugin;
        this.menuMap = new HashMap<>();
        this.menuFileMap = new HashMap<>();
        this.menuConfigMap = new HashMap<>();
        this.menuIds = new ArrayList<>();

    }

    public void setup() {
        directory = new File(plugin.getDataFolder(), "/menus");
        if (!directory.exists()) {
            directory.mkdir();
            plugin.getLogger().info("Couldn't find the menus directory, generating a new one!");
        }

        if (directory.listFiles() == null) {
             File exampleFile = new File(plugin.getDataFolder() + "/menus", "example.yml");
             plugin.saveResource("menus/example.yml",false);
             YamlConfiguration configuration = YamlConfiguration.loadConfiguration(exampleFile);
             plugin.getLogger().info("No files found in the directory, generating an example menu...");

             plugin.getLogger().info(configuration.getCurrentPath());
             plugin.getLogger().info("test: " + configuration.getInt("size"));
             new BukkitRunnable() {
                 public void run() {
                     Menu exampleMenu = new Menu(plugin, configuration);
                     String menuId = exampleFile.getName();

                     menuMap.put(menuId, exampleMenu);
                     menuIds.add(menuId);
                     menuFileMap.put(menuId, exampleFile);
                     menuConfigMap.put(menuId, configuration);
                 }
             }.runTaskLater(plugin, 3);

             return;
        }

        for (File file: directory.listFiles()) {
            try {
                menuFileMap.put(file.getName(), file);

                YamlConfiguration menuConfig = YamlConfiguration.loadConfiguration(file);
                menuConfigMap.put(file.getName(), menuConfig);
                menuIds.add(file.getName());

                Menu menu = new Menu(plugin, menuConfig);
                menuMap.put(file.getName(), menu);
            }
            catch (Exception exception) {
                plugin.getLogger().info("Error whilst loading " + file.getName() + "!");
                exception.printStackTrace();
            }
        }
    }

    public boolean isMenu(String menuId) {
        if (menuIds.contains(menuId)) {
            return true;
        }
        for (File file: directory.listFiles()) {
            if (file.getName().equalsIgnoreCase(menuId)) {
                return true;
            }
        }
        return false;
    }

    public void openMenu(Player player, Menu menu) {
        menu.build();
        new BukkitRunnable() {
            @Override
            public void run(){
                player.openInventory(menu.getInv());
            }
        }.runTaskLater(plugin, 4);
    }

    public void createMenu(String menuId) {
        File customFile = new File(directory.getAbsoluteFile(), menuId+ ".yml");
        try {
            customFile.createNewFile();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(customFile);

            config.set("name", menuId);
            config.set("size", 54);
            config.set("permission", "");
            config.set("frame1.slots", "0,1,2,3,4,5,6,7,8,9");
            config.set("frame1.item.material", Material.GRAY_STAINED_GLASS.toString());
            config.set("frame1.item.name", "example_item");
            config.set("frame1.item.customModelData", null);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("line 1");
            lore.add("line 2");
            lore.add("line 3");
            config.set("frame1.item.lore", lore.listIterator());
            config.set("frame1.actions.closeInventory", true);
            ArrayList<String> messages = new ArrayList<>();
            messages.add("hey %player%");
            config.set("frame1.actions.sendMessage", messages.listIterator());
            ArrayList<String> commandsByPlayer = new ArrayList<>();
            commandsByPlayer.add("say hi %player%!");
            config.set("frame1.actions.commands.byPlayer", commandsByPlayer.listIterator());
            ArrayList<String> commandsByConsole = new ArrayList<>();
            commandsByConsole.add("say hi from console %player%");
            config.set("frame1.actions.commands.byConsole", commandsByConsole.listIterator());
            config.set("0.item.material", Material.DIAMOND.toString());
            config.set("0.item.name", "example_slot_item");
            config.set("0.item.customModelData", 999999999);
            config.set("0.item.lore", lore.listIterator());
            config.set("0.actions.permission", "");
            config.set("0.actions.sendMessage", messages.listIterator());
            config.set("0.actions.closeInventory", true);
            config.set("0.actions.commands.byPlayer", commandsByPlayer.listIterator());
            config.set("0.actions.commands.byConsole", commandsByConsole.listIterator());
            try {
                config.save(customFile);
                YamlConfiguration x = YamlConfiguration.loadConfiguration(customFile);
                Menu menu = new Menu(plugin, x);
                menuMap.put(menuId, menu);
                menuFileMap.put(menuId, customFile);
                menuConfigMap.put(menuId, x);
                menuIds.add(menuId + ".yml");
                reloadMenu(menuId + ".yml");
            }catch (Exception ex) {ex.printStackTrace();}
            //e
        }catch (Exception ex) {
            plugin.getLogger().info("Error! Whilst creating a new file... dm knovius ");
            ex.printStackTrace();
        }
    }

    public void removeMenu(String menuId) {
        closeMenu(menuMap.get(menuId));
        menuMap.remove(menuId);
        menuIds.remove(menuId);
        File file = menuFileMap.get(menuId);
        file.delete();
        menuFileMap.remove(menuId);
        menuConfigMap.remove(menuId);

    }

    // for clean reload
    public void closeAllMenus() {
        for (PlayerData playerData : EpicMenu.getInstance().getPlayerManager().getPlayerDataMap().values()) {
            if (playerData.getPlayer().getInventory() == playerData.getOpenedMenu()) {
                playerData.getPlayer().closeInventory();
            }
        }
    }

    // closes all player's inventory with this specific menu open
    public void closeMenu(Menu menu) {
        //new menu pog
        if (menu == null || menu.getInv() == null) return;
        // old menus
        for (PlayerData playerData : EpicMenu.getInstance().getPlayerManager().getPlayerDataMap().values()) {
            if (playerData.getPlayer().getInventory() == menu.getInv()) {
                playerData.getPlayer().closeInventory();
            }
        }
    }

    public void reloadMenu(String menuId) {
        Menu menu = menuMap.get(menuId);
        closeMenu(menu);
        menuMap.remove(menuId, menu);
        menuFileMap.remove(menuId);
        menuConfigMap.remove(menuId);

        new BukkitRunnable() {
            @Override
            public void run() {
                //methods
                int end = 1;
                for (File file : directory.listFiles()) {
                    if (file.getName().equalsIgnoreCase(menuId)) {
                        menuFileMap.put(menuId, file);
                        YamlConfiguration menuConfig = YamlConfiguration.loadConfiguration(file);
                        menuConfigMap.put(menuId, menuConfig);
                        Menu menu = new Menu(plugin, menuConfig);
                        menuMap.put(menuId, menu);
                    }
                    if (end++ == directory.listFiles().length) {
                        menuIds.remove(menuId);
                    }
                }
            }
        }.runTaskLater(plugin, 3);
    }


    public void reloadAllMenu() {

        closeAllMenus();
        menuMap.clear();
        menuFileMap.clear();
        menuConfigMap.clear();
        menuIds.clear();

        for (File file: directory.listFiles()) {
            try {
                menuFileMap.put(file.getName(), file);
                YamlConfiguration menuConfig = YamlConfiguration.loadConfiguration(file);
                menuConfigMap.put(file.getName(), menuConfig);

                Menu menu = new Menu(plugin, menuConfig);
                menuMap.put(file.getName(), menu);
                menuIds.add(file.getName());
            }
            catch (Exception exception) {
                plugin.getLogger().info("Error whilst loading " + file.getName() + "!");
                exception.printStackTrace();
            }
        }
    }
}
