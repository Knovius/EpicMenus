package me.knovius.epicmenu.menu.object;

import lombok.Getter;
import me.knovius.epicmenu.EpicMenu;
import me.knovius.epicmenu.frame.Frame;
import me.knovius.epicmenu.slots.Slot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Getter
public class Menu {

    private EpicMenu plugin;

    private Inventory inv;

    private YamlConfiguration config;
    private String id;
    private String permission;
    private String menuName;
    private int size;
    private List<Frame> frames;
    private HashMap<Integer, Slot> slotMap;



    public Menu(EpicMenu plugin, YamlConfiguration config) {
        this.plugin = plugin;
        this.config = config;
        this.id = config.getName();
        this.size = config.getInt("size");
        this.menuName = config.getString("name");
        this.permission = config.getString("permission");
        this.inv = Bukkit.createInventory(null, size, menuName);
        this.slotMap = new HashMap<>();
        this.frames = new ArrayList<>();
    }


    public void setupFrames() {
        for (String key: config.getKeys(false)) {
            if (key.toLowerCase().contains("frame")) {

                try {
                    Frame frame = new Frame(plugin, config.getConfigurationSection(key));
                    for (Slot slot : frame.getSlotsInFrame()) {
                        slotMap.put(slot.getSlotId(), slot);
                    }
                    frames.add(frame);
                    frame.build(this);
                } catch (Exception ex) {
                    plugin.getLogger().info("Error! " + key + " frame in the " + menuName + " menu can't be loaded!");
                    ex.printStackTrace();
                }
            }
        }
    }

    public void setupSlots() {
        for (String key: config.getKeys(false)) {
            if (key.toLowerCase().contains("frame") || key.toLowerCase().contains("name") || key.toLowerCase().contains("permission") || key.toLowerCase().contains("size")) continue;

            try {
                Slot slot = new Slot(config.getConfigurationSection(key), Integer.parseInt(key), false);
                slotMap.put(slot.getSlotId(), slot);
            }catch (Exception ex) {
                plugin.getLogger().info("Error! " + key + " slot in the " + menuName + " menu can't be loaded!");
                ex.printStackTrace();
            }
        }
    }

    public Slot getSlot(int slotPlacement) {
        return  slotMap.get(slotPlacement);
    }

    public void build() {
        //Building Frames
        setupFrames();

        //Building Slots

        setupSlots();
        new BukkitRunnable() {

            public void run() {
                for (Slot slot : slotMap.values()) {
                    if (!slot.isFrame()) {
                        inv.setItem(slot.getSlotId(), slot.toItem());
                    }
                }
            }
        }.runTaskLater(plugin, 2);
    }

    public void open(Player player) {
        build();
        player.openInventory(inv);
    }
}
