package me.jsinco.customsaplings.util;

import me.jsinco.customsaplings.CustomSaplings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Util {

    private final static CustomSaplings plugin = CustomSaplings.getInstance();



    public static void giveItem(Player player, ItemStack item) {
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).isSimilar(item)) {
                player.getInventory().addItem(item);
                break;
            } else if (i == 35) {
                player.getWorld().dropItem(player.getLocation(), item);
            }
        }
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(TextUtils.colorcode(plugin.getConfig().getString("prefix") + msg));
    }

    public static void debugLog(String msg) {
        if (plugin.getConfig().getBoolean("verbose-logging")) {
            Bukkit.getConsoleSender().sendMessage(TextUtils.colorcode(plugin.getConfig().getString("prefix") + msg));
        }
    }

    @Nullable
    public static <T> T getRand(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get((int) (Math.random() * list.size()));
    }
}
