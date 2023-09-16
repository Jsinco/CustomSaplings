package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand implements SubCommand {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("customsaplings.helpcommand")) {
            sender.sendMessage(TextUtils.prefix + "You do not have permission to use this command!");
            return;
        }

        String helpMessage = TextUtils.colorcode("""
                &#9ad36e&lC&#9ad671&lu&#99d974&ls&#99dd77&lt&#99e07a&lo&#98e37d&lm&#98e680&lS&#98ea82&la&#98ed85&lp&#97f088&ll&#97f38b&li&#97f78e&ln&#96fa91&lg&#96fd94&ls &rHelp
                &e/customsaplings help &7- Displays this help message
                &e/customsaplings paste &7- Pastes a schematic at the block you are looking at
                &e/customsaplings give <?player> &7- Gives you a custom sapling ? gives to another player if specified
                &e/customsaplings reload &7- Reloads the plugin
                """);
        sender.sendMessage(helpMessage);
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return null;
    }
}
