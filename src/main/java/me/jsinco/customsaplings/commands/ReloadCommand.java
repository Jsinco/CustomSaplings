package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.FileManager;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand implements SubCommand {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("customsaplings.reloadcommand")) {
            sender.sendMessage(TextUtils.prefix + "You do not have permission to use this command!");
            return;
        }
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        FileManager fileManager = new FileManager(plugin);
        fileManager.loadSchematicFiles();
        fileManager.loadSaplingsFile();
        fileManager.loadDefaultConfig(true);
        TextUtils.reloadTextUtils();

        sender.sendMessage(TextUtils.prefix + "Reloaded!");
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return null;
    }
}
