package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.util.TextUtils;
import me.jsinco.customsaplings.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VerboseCommand implements SubCommand {
    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("customsaplings.verbose")) {
            sender.sendMessage(TextUtils.prefix + "You do not have permission to use this command!");
            return;
        }

        if ((args.length >= 2 && args[1].equals("console")) || !(sender instanceof Player player)) {
            plugin.getConfig().set("verbose-logging", !plugin.getConfig().getBoolean("verbose-logging"));
            plugin.saveConfig();
            sender.sendMessage(TextUtils.prefix + "Verbose logging is now toggled in config.yml");
            return;
        }

        if (Util.isVerbosePlayer(player)) {
            Util.removeVerbosePlayer(player);
        } else {
            Util.addVerbosePlayer(player);
        }

    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender sender, String[] args) {
        return List.of("console");
    }
}
