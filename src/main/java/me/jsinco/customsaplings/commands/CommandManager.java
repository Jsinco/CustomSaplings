package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
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
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        args[0] = args[0].toLowerCase();

        if (!subCommands.containsKey(args[0])) {
            sender.sendMessage("Unknown subcommand!");
            subCommands.get("help").execute(plugin, sender, args);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);
        subCommand.execute(plugin, sender, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
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
