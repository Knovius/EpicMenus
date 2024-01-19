package me.knovius.epicmenu.frame;

import lombok.Getter;
import me.knovius.epicmenu.EpicMenu;
import me.knovius.epicmenu.menu.object.Menu;
import me.knovius.epicmenu.slots.Slot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private EpicMenu plugin;
    private ConfigurationSection path;

    @Getter
    private List<Slot> slotsInFrame;

    public Frame(EpicMenu plugin, ConfigurationSection path) {
        this.plugin = plugin;
        this.path = path ;
        slotsInFrame = new ArrayList<>();

        for (String x :path.getString("slots").split(",")) {
            Slot slot = new Slot(path, Integer.parseInt(x), true);
            slotsInFrame.add(slot);
        }
    }

    public void build(Menu menu) {
        for (Slot slot: slotsInFrame) {
            menu.getInv().setItem(slot.getSlotId(), slot.toItem());
        }
    }


}
