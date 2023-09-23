package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BoxCommand implements SubCommand {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("customsaplings.boxcommand") || !(sender instanceof Player player)) {
            sender.sendMessage(TextUtils.prefix + "You do not have permission to use this command!");
            return;
        } else if (args.length < 2) {
            sender.sendMessage(TextUtils.prefix + "You must specify a sapling box name!");
            return;
        }

        String boxName = args[1];
        ItemStack boxItem = new ItemStack(Material.valueOf(plugin.getConfig().getString("rarity-boxes." + boxName + ".material").toUpperCase()));
        if (boxItem.getType().equals(Material.PLAYER_HEAD)) {
            SkullMeta skullMeta = (SkullMeta) boxItem.getItemMeta();

            String data = plugin.getConfig().getString("rarity-boxes." + boxName + ".data");
            if (data != null) {
                skullMeta.setOwner(data);
            }
            if (plugin.getConfig().get("rarity-boxes." + boxName + ".enchant-glint") != null && plugin.getConfig().getBoolean("rarity-boxes." + boxName + ".enchant-glint")) {
                skullMeta.addEnchant(Enchantment.LUCK, 1, true);
                skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            skullMeta.setDisplayName(TextUtils.colorcode(plugin.getConfig().getString("rarity-boxes." + boxName + ".name")));
            skullMeta.setLore(TextUtils.colorArrayList(plugin.getConfig().getStringList("rarity-boxes." + boxName + ".lore")));
            skullMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "box"), PersistentDataType.STRING, boxName);
            boxItem.setItemMeta(skullMeta);
        } else {
            ItemMeta meta = boxItem.getItemMeta();
            int data = plugin.getConfig().getInt("rarity-boxes." + boxName + ".data");
            if (data != 0) {
                meta.setCustomModelData(data);
            }
            if (plugin.getConfig().get("rarity-boxes." + boxName + ".enchant-glint") != null && plugin.getConfig().getBoolean("rarity-boxes." + boxName + ".enchant-glint")) {
                meta.addEnchant(Enchantment.LUCK, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            meta.setDisplayName(TextUtils.colorcode(plugin.getConfig().getString("rarity-boxes." + boxName + ".name")));
            meta.setLore(TextUtils.colorArrayList(plugin.getConfig().getStringList("rarity-boxes." + boxName + ".lore")));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "box"), PersistentDataType.STRING, boxName);
            boxItem.setItemMeta(meta);
        }

        player.getInventory().addItem(boxItem);
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (args.length == 2) {
            return plugin.getConfig().getConfigurationSection("rarity-boxes").getKeys(false)
                    .stream()
                    .toList();
        }
        return null;
    }
}
