package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    /**
     * Executes the subcommand
     * @param plugin The plugin instance so each subcommand has an instance of the plugin
     * @param sender The sender of the command
     * @param args The arguments of the command
     */
    void execute(CustomSaplings plugin, CommandSender sender, String[] args);

    /**
     * Allows subcommands to have tab completion
     * @param plugin The plugin instance so each subcommand has an instance of the plugin
     * @param sender The sender of the command
     * @param args The arguments of the command
     * @return A list of possible tab completions
     */
    List<String> tabComplete(CustomSaplings plugin, CommandSender sender, String[] args);
}
