package me.jsinco.customsaplings.util;

import me.jsinco.customsaplings.CustomSaplings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Util {

    private final static CustomSaplings plugin = CustomSaplings.getPlugin(CustomSaplings.class);
    private static final List<Player> verbosePlayers = new ArrayList<>();

    public static void addVerbosePlayer(Player player) {
        verbosePlayers.add(player);
        log("&a" + player.getName() + " enabled verbose logging!");
    }

    public static boolean isVerbosePlayer(Player player) {
        return verbosePlayers.contains(player);
    }

    public static void removeVerbosePlayer(Player player) {
        verbosePlayers.remove(player);
        log("&c" + player.getName() + " disabled verbose logging!");
    }

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
        if (plugin.getConfig().getBoolean("verbose-logging")) {
            Bukkit.getConsoleSender().sendMessage(TextUtils.colorcode(plugin.getConfig().getString("prefix") + msg));
        }
        for (Player player : verbosePlayers) {
            if (player == null) {
                verbosePlayers.remove(null);
                continue;
            }
            player.sendMessage(TextUtils.colorcode(plugin.getConfig().getString("prefix") + msg));
        }
    }
}
