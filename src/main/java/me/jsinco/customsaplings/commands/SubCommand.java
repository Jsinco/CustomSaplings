package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    void execute(CustomSaplings plugin, CommandSender sender, String[] args);

    List<String> onTabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args);
}
