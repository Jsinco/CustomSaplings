package me.jsinco.customsaplings;

import me.jsinco.customsaplings.commands.CommandManager;
import me.jsinco.customsaplings.listeners.Events;
import org.bukkit.plugin.java.JavaPlugin;

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

        getCommand("customsaplings").setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new Events(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
