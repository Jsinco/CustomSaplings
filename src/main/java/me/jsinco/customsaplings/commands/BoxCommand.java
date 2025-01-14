package me.jsinco.customsaplings.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.util.TextUtils;
import me.jsinco.customsaplings.util.Util;
import org.bukkit.Bukkit;
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
import java.util.UUID;

public class BoxCommand implements SubCommand {

    private static final UUID uuid = UUID.fromString("f1461958-c3f4-4d0c-bca7-618f746ee800");

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("customsaplings.boxcommand")) {
            sender.sendMessage(TextUtils.prefix + "You do not have permission to use this command!");
            return;
        } else if (args.length < 3) {
            sender.sendMessage(TextUtils.prefix + "/customsaplings box <rarity> <player>");
            return;
        }

        String boxName = args[1];
        Player player = Bukkit.getPlayerExact(args[2]);
        if (player == null) {
            sender.sendMessage(TextUtils.prefix + "That player is not online!");
            return;
        }


        ItemStack boxItem = new ItemStack(Material.valueOf(plugin.getConfig().getString("rarity-boxes." + boxName + ".material").toUpperCase()));

        ItemMeta meta = boxItem.getItemMeta();

        if (plugin.getConfig().get("rarity-boxes." + boxName + ".enchant-glint") != null && plugin.getConfig().getBoolean("rarity-boxes." + boxName + ".enchant-glint")) {
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setDisplayName(TextUtils.colorcode(plugin.getConfig().getString("rarity-boxes." + boxName + ".name")));
        meta.setLore(TextUtils.colorArrayList(plugin.getConfig().getStringList("rarity-boxes." + boxName + ".lore")));
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "box"), PersistentDataType.STRING, boxName);

        if (meta instanceof SkullMeta skullMeta) {
            String data = plugin.getConfig().getString("rarity-boxes." + boxName + ".data");
            if (data != null) {
                PlayerProfile profile = Bukkit.createProfile(uuid);
                profile.setProperty(new ProfileProperty("textures", data));
                skullMeta.setPlayerProfile(profile);
            }
        } else {
            int data = plugin.getConfig().getInt("rarity-boxes." + boxName + ".data");
            if (data != 0) {
                meta.setCustomModelData(data);
            }
        }

        boxItem.setItemMeta(meta);
        Util.giveItem(player, boxItem);
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
