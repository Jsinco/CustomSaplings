package me.jsinco.customsaplings;

import me.jsinco.customsaplings.commands.CommandManager;
import me.jsinco.customsaplings.listeners.Events;
import me.jsinco.customsaplings.util.Util;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CustomSaplings extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        FileManager fileManager = new FileManager(this);
        fileManager.loadSchematicFiles();
        fileManager.loadSaplingsFile();
        fileManager.loadDefaultConfig(false);

        YamlConfiguration rawConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
        if (rawConfig.get("version") == null || !rawConfig.getString("version").equals(getDescription().getVersion())) {
            fileManager.updateFile("config.yml"); // Update config.yml if it's outdated
            Util.log("&dUpdated config.yml to version " + getDescription().getVersion());
        }

        // Register commands and events
        getCommand("customsaplings").setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new Events(this), this);
        Util.log("&aEnabled Custom Saplings v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        Util.log("&aDisabled Custom Saplings v" + getDescription().getVersion());
    }
}
