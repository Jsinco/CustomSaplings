package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.util.TextUtils;
import me.jsinco.customsaplings.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final CustomSaplings plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public CommandManager(CustomSaplings plugin) {
        this.plugin = plugin;

        // Subcommands
        subCommands.put("help", new HelpCommand());
        subCommands.put("paste", new PasteCommand());
        subCommands.put("give", new GiveCommand());
        subCommands.put("reload", new ReloadCommand());
        subCommands.put("box", new BoxCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            Util.debugLog("&a" + sender.getName() + " executed /customsaplings");
            sender.sendMessage(TextUtils.prefix + "Custom Saplings v" + plugin.getDescription().getVersion() + " by " + plugin.getDescription().getAuthors().get(0));
            return true;
        }

        args[0] = args[0].toLowerCase();
        Util.debugLog("&a" + sender.getName() + " executed /customsaplings " + String.join(" ", args));
        if (!subCommands.containsKey(args[0])) {
            sender.sendMessage(TextUtils.prefix + "Unknown subcommand!");
            subCommands.get("help").execute(plugin, sender, args);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);
        subCommand.execute(plugin, sender, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customsaplings.tabcomplete")) {
            return null;
        }
        if (args.length == 1) {
            return subCommands.keySet().stream().toList();
        }

        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand != null) {
            return subCommand.tabComplete(plugin, sender, args);
        }

        return null;
    }
}
