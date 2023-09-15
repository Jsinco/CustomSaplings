package me.jsinco.customsaplings;

import org.bukkit.plugin.java.JavaPlugin;

// I can't believe there isn't a plugin for this on spigot : 9/15/2021
public final class CustomSaplings extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        new FileManager(this).loadSchematicFiles();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
