package me.jsinco.customsaplings;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Saplings {

    private static final CustomSaplings plugin = CustomSaplings.getPlugin(CustomSaplings.class);


    public static ItemStack getSapling(String saplingName) {
        try {
            ItemStack sapling = new ItemStack(Material.valueOf(FileManager.getSaplingsFile(plugin).getString(saplingName + ".material").toUpperCase()));
            ItemMeta meta = sapling.getItemMeta();
            meta.setDisplayName(TextUtils.colorcode(FileManager.getSaplingsFile(plugin).getString(saplingName + ".name")));
            meta.setLore(TextUtils.colorArrayList(FileManager.getSaplingsFile(plugin).getStringList(saplingName + ".lore")));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "schematic"), PersistentDataType.STRING, FileManager.getSaplingsFile(plugin).getString(saplingName + ".schematic"));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "sapling"), PersistentDataType.STRING, saplingName);
            sapling.setItemMeta(meta);
            return sapling;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static List<ItemStack> getAllSaplings() {
        List<String> saplingNames = List.copyOf(FileManager.getSaplingsFile(plugin).getKeys(false));
        List<ItemStack> saplings = new ArrayList<>();
        for (String saplingName : saplingNames) {
            ItemStack sapling = getSapling(saplingName);
            if (sapling != null) saplings.add(sapling);

        }
        return saplings;
    }


    public static void setTree(String fileName, Block block) {
        File file = FileManager.getSchematicFile(plugin, fileName);
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            Schematic schematic = new Schematic(clipboard);

            schematic.paste(block.getLocation());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
