package me.jsinco.customsaplings;

import me.jsinco.customsaplings.commands.CommandManager;
import me.jsinco.customsaplings.listeners.Events;
import org.bukkit.plugin.java.JavaPlugin;

// I can't believe there isn't a plugin for this on spigot : 9/15/2021
public final class CustomSaplings extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        FileManager fileManager = new FileManager(this);
        fileManager.loadSchematicFiles();
        fileManager.loadSaplingsFile();
        fileManager.loadDefaultConfig(false);

        getCommand("customsaplings").setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new Events(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
