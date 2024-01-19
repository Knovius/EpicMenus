package me.knovius.epicmenu;

import lombok.Getter;
import me.knovius.epicmenu.commands.MenuCommand;
import me.knovius.epicmenu.data.PlayerManager;
import me.knovius.epicmenu.listeners.InventoryListener;
import me.knovius.epicmenu.menu.MenuListener;
import me.knovius.epicmenu.menu.MenuManager;
import me.knovius.epicmenu.menu.events.MenuClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EpicMenu extends JavaPlugin {

    @Getter
    public static EpicMenu instance;

    @Getter
    private MenuManager menuManager;

    @Getter
    private PlayerManager playerManager;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        menuManager = new MenuManager(this);
        playerManager = new PlayerManager(this);

        //registering command
        getCommand("epicmenu").setExecutor(new MenuCommand());

        //registering events

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new MenuListener(), this);
        pm.registerEvents(new InventoryListener(), this);

        // config
        saveDefaultConfig();

        // setup

        menuManager.setup();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
