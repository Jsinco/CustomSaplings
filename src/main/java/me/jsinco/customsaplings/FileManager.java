package me.jsinco.customsaplings;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileManager {

    File[] schematics;
    private final CustomSaplings plugin;

    public FileManager(CustomSaplings plugin) {
        this.plugin = plugin;
    }


    /**
     * Loads all schematic files from the schematics folder
     */
    public void loadSchematicFiles() {
        File schematicsFolder = new File(plugin.getDataFolder(), "Schematics");
        if (!schematicsFolder.exists()) {
            schematicsFolder.mkdir();
        }

        schematics = schematicsFolder.listFiles();
        if (schematics != null && schematics.length == 0) {
            loadDefaultSchematics();
        }
    }

    /**
     * Loads the default schematics from the jar file
     */
    public void loadDefaultSchematics() {
        String[] defaultSchematics = {"gray_tree.schem", "maple_tree.schem", "mushpeach_cherrytree.schem", "bigtree.schem"};
        for (String fileName : defaultSchematics) {
            File file = new File(plugin.getDataFolder(), "Schematics" + File.separator + fileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                    InputStream inputStream = plugin.getResource("Schematics" + File.separator + fileName);
                            OutputStream outputStream = Files.newOutputStream(file.toPath());
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Gets a schematic file from the schematics folder
     * @param plugin The plugin instance
     * @param schematicName The name of the schematic file
     * @return The schematic file
     */
    public static File getSchematicFile(CustomSaplings plugin, String schematicName) {
        File schemFile = new File(plugin.getDataFolder(), "Schematics" + File.separator + schematicName);
        if (!schemFile.exists()) {
            plugin.getLogger().warning("Schematic file " + schematicName + " does not exist!");
            return null;
        }
        return schemFile;
    }

    /**
     * Loads the saplings.yml file
     */
    public void loadSaplingsFile() {
        File saplingsFile = new File(plugin.getDataFolder(), "saplings.yml");
        try {
            if (!saplingsFile.exists()) {
                saplingsFile.createNewFile();
                InputStream inputStream = plugin.getResource("saplings.yml");
                OutputStream outputStream = Files.newOutputStream(saplingsFile.toPath());
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the saplings.yml file
     * @param plugin The plugin instance
     * @return The saplings.yml file
     */
    public static YamlConfiguration getSaplingsFile(CustomSaplings plugin) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "saplings.yml"));
    }


    // TODO: Should this be a method
    public void loadDefaultConfig(boolean reload) {
        if (!reload) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();

        } else {
            plugin.reloadConfig();
        }
    }
}
