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
                &#a8ff92/customsaplings help &#E2E2E2- Displays this help message
                &#a8ff92/customsaplings paste &#E2E2E2- Pastes a schematic at the block you are looking at
                &#a8ff92/customsaplings give <?player> &#E2E2E2- Gives you a custom sapling ? gives to another player if specified
                &#a8ff92/customsaplings reload &#E2E2E2- Reloads the plugin
                """);
        sender.sendMessage(TextUtils.colorcode("&#9ad36e&lC&#9ad671&lu&#99d974&ls&#99dd77&lt&#99e07a&lo&#98e37d&lm&#98e680&lS&#98ea82&la&#98ed85&lp&#97f088&ll&#97f38b&li&#97f78e&ln&#96fa91&lg&#96fd94&ls &#E2E2E2v" + plugin.getDescription().getVersion() + " by " + plugin.getDescription().getAuthors().get(0)));
        sender.sendMessage(helpMessage);
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return null;
    }
}
