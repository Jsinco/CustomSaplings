package me.jsinco.customsaplings.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utility {

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
}
