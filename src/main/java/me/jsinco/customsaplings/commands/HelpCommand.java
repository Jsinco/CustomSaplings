package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand implements SubCommand {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        String helpMessage = """
                &6&lCustom Saplings Help
                &e/csap help &7- Displays this help message
                &e/csap paste &7- Pastes the schematic at your location
                """;
        sender.sendMessage(helpMessage);
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return null;
    }
}
