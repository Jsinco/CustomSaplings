package me.jsinco.customsaplings.manager;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.obj.MultiSaplingContainer;
import me.jsinco.customsaplings.obj.SaplingContainer;
import me.jsinco.customsaplings.obj.Schematic;
import me.jsinco.customsaplings.obj.SingleSaplingContainer;
import me.jsinco.customsaplings.util.TextUtils;
import me.jsinco.customsaplings.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SaplingsManager {

    private static final Map<String, SaplingContainer> saplingContainers = new HashMap<>();

    private static final CustomSaplings plugin = CustomSaplings.getInstance();
    //private static final NamespacedKey schematicKey = new NamespacedKey(plugin, "schematic");
    private static final NamespacedKey saplingKey = new NamespacedKey(plugin, "sapling");


    public static void reloadSaplingContainers() {
        if (!saplingContainers.isEmpty()) {
            saplingContainers.clear();
            Util.log("&aCleared sapling containers!");
        }
        YamlConfiguration saplingsFile = FileManager.getSaplingsFile(plugin);

        for (String saplingSectionName : saplingsFile.getKeys(false)) {
            ConfigurationSection saplingSection = saplingsFile.getConfigurationSection(saplingSectionName);

            Material material = Material.valueOf(saplingSection.getString("material", "OAK_SAPLING").toUpperCase());
            String name = saplingSection.getString("name", "Unnamed Sapling");
            List<String> lore = saplingSection.getStringList("lore");
            boolean enchantGlint = saplingSection.getBoolean("enchant-glint", false);
            int customModelData = saplingSection.getInt("custom-model-data", 0);
            String rarityBox = saplingSection.getString("rarity-box", null);


            if (saplingSection.get("schematic") instanceof Collection<?>) {
                saplingContainers.put(saplingSectionName, new MultiSaplingContainer(material, name, lore, enchantGlint, customModelData, rarityBox, saplingSectionName, saplingSection.getStringList("schematic")));
            } else {
                saplingContainers.put(saplingSectionName, new SingleSaplingContainer(material, name, lore, enchantGlint, customModelData, rarityBox, saplingSectionName, saplingSection.getString("schematic")));
            }

        }
        Util.log("&aFinished loading " + saplingContainers.size() + " saplings!");
    }


    @Nullable
    public static String getSaplingSchematic(String saplingName) {
        SaplingContainer saplingContainer = saplingContainers.get(saplingName);
        if (saplingContainer != null) {
            return saplingContainer.getSchematic();
        }
        return null;
    }

    @Nullable
    public static ItemStack getSaplingItem(String saplingName) {

        SaplingContainer saplingContainer = saplingContainers.get(saplingName);
        if (saplingContainer == null) {
            Util.log("&eCould not find sapling " + saplingName + "! (Maybe try /customsaplings reload?)");
            return null;
        }

        ItemStack sapling = new ItemStack(saplingContainer.material());
        ItemMeta meta = sapling.getItemMeta();
        meta.setDisplayName(TextUtils.colorcode(saplingContainer.name()));
        meta.setLore(TextUtils.colorArrayList(saplingContainer.lore()));
        meta.getPersistentDataContainer().set(saplingKey, PersistentDataType.STRING, saplingContainer.getId());
        //meta.getPersistentDataContainer().set(schematicKey, PersistentDataType.STRING, saplingContainer.getSchematic());

        if (saplingContainer.enchantGlint()) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (saplingContainer.customModelData() != 0) {
            meta.setCustomModelData(saplingContainer.customModelData());
        }

        sapling.setItemMeta(meta);
        return sapling;
    }


    public static List<ItemStack> getAllSaplings() {
        List<ItemStack> saplings = new ArrayList<>();
        for (String saplingName : saplingContainers.keySet()) {
            saplings.add(getSaplingItem(saplingName));
        }
        return saplings;
    }

    public static List<String> getAllSaplingsOfRarity(String rarity) {
        List<String> returnSaplings = new ArrayList<>();
        for (String saplingName : saplingContainers.keySet()) {
            SaplingContainer saplingContainer = saplingContainers.get(saplingName);

            if (saplingContainer.rarityBox() != null && rarity.equalsIgnoreCase(saplingContainer.rarityBox())) {
                returnSaplings.add(saplingName);
            }
        }
        return returnSaplings;
    }


    public static boolean setSchematic(String fileName, Location loc) {
        File file = FileManager.getSchematicFile(plugin, fileName);
        if (file == null && plugin.getConfig().getBoolean("search-worldedit-schematics-folder")) {
            Util.debugLog("&dCould not find schematic file " + fileName + " in the plugin folder, searching WorldEdit/FAWE schematics folder...");
            file = FileManager.getSchematicFileFromWE(plugin, fileName);
        }
        if (file == null) {
            Util.log("&cSomeone tried to set a schematic that does not exist! File: " + fileName + " Location: "+ loc);
            return false;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            Util.log("&cCould not find a clipboard format for file " + fileName + "!");
            return false;
        }
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            Schematic schematic = new Schematic(clipboard);

            schematic.paste(loc);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
