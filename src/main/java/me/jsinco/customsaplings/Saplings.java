package me.jsinco.customsaplings;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import me.jsinco.customsaplings.util.TextUtils;
import me.jsinco.customsaplings.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Saplings {

    private static final CustomSaplings plugin = CustomSaplings.getInstance();
    private static final NamespacedKey schematicKey = new NamespacedKey(plugin, "schematic");
    private static final NamespacedKey saplingKey = new NamespacedKey(plugin, "sapling");

    @Nullable
    public static ItemStack getSapling(String saplingName) {

        ConfigurationSection saplingSec = FileManager.getSaplingsFile(plugin).getConfigurationSection(saplingName);
        if (saplingSec == null) return null;

        ItemStack sapling = new ItemStack(Material.valueOf(saplingSec.getString("material", "OAK_SAPLING").toUpperCase()));
        ItemMeta meta = sapling.getItemMeta();
        meta.setDisplayName(TextUtils.colorcode(saplingSec.getString("name", "Unnamed Sapling")));
        meta.setLore(TextUtils.colorArrayList(saplingSec.getStringList("lore")));
        meta.getPersistentDataContainer().set(saplingKey, PersistentDataType.STRING, saplingName);


        String schematicString = saplingSec.getString("schematic");
        if (schematicString == null) {
            Util.debugLog("Getting random schematic for sapling " + saplingName);
            schematicString = Util.getRand(saplingSec.getStringList("schematic"));
        }
        if (schematicString == null) {
            Util.log("&eCould not find a schematic for sapling " + saplingName + "! Setting to 'unknown_tree.schem'");
            schematicString = "unknown_tree.schem";
        }

        meta.getPersistentDataContainer().set(schematicKey, PersistentDataType.STRING, schematicString);

        if (FileManager.getSaplingsFile(plugin).get(saplingName + ".enchant-glint") != null && FileManager.getSaplingsFile(plugin).getBoolean(saplingName + ".enchant-glint")) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (FileManager.getSaplingsFile(plugin).get(saplingName + ".custom-model-data") != null) {
            meta.setCustomModelData(FileManager.getSaplingsFile(plugin).getInt(saplingName + ".custom-model-data"));
        }

        sapling.setItemMeta(meta);
        return sapling;

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


    public static List<String> getAllSaplingsOfRarity(String rarity) {
        List<String> saplingNames = new ArrayList<>(FileManager.getSaplingsFile(plugin).getKeys(false).stream().toList());
        List<String> returnSaplings = new ArrayList<>();
        for (String saplingName : saplingNames) {
            if (FileManager.getSaplingsFile(plugin).getString(saplingName + ".rarity-box") == null) continue;
            if (FileManager.getSaplingsFile(plugin).getString(saplingName + ".rarity-box").equalsIgnoreCase(rarity)) {
                returnSaplings.add(saplingName);
            }
        }
        return returnSaplings;
    }

    public static void setSchematic(String fileName, Location loc) {
        File file = FileManager.getSchematicFile(plugin, fileName);
        if (file == null && plugin.getConfig().getBoolean("search-worldedit-schematics-folder")) {
            Util.debugLog("&dCould not find schematic file " + fileName + " in the plugin folder, searching WorldEdit/FAWE schematics folder...");
            file = FileManager.getSchematicFileFromWE(plugin, fileName);
        }
        if (file == null) {
            Util.log("&eSomeone tried to set a schematic that does not exist! File: " + fileName + " Location: "+ loc);
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            Schematic schematic = new Schematic(clipboard);

            schematic.paste(loc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
