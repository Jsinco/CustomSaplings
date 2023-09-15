package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PasteCommand implements SubCommand {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {

    }

    @Override
    public List<String> onTabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return null;
    }
}
