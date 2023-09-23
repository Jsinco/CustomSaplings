package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.FileManager;
import me.jsinco.customsaplings.Saplings;
import me.jsinco.customsaplings.util.TextUtils;
import me.jsinco.customsaplings.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiveCommand implements SubCommand {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("customsaplings.givecommand")) {
            sender.sendMessage(TextUtils.prefix + "You do not have permission to use this command!");
            return;
        } else if (args.length < 2) {
            sender.sendMessage(TextUtils.prefix + "You must specify a sapling name!");
            return;
        }

        ItemStack sapling = Saplings.getSapling(args[1]);
        Player deliverTo = args.length == 2 && sender instanceof Player ? (Player) sender : Bukkit.getPlayerExact(args[2]);
        if (sapling == null) {
            sender.sendMessage(TextUtils.prefix + "That sapling does not exist!");
            return;
        } else if (deliverTo == null) {
            sender.sendMessage(TextUtils.prefix + "That player is not online!");
            return;
        }

        Utility.giveItem(deliverTo, sapling);
        if (args.length > 2) {
            sender.sendMessage(TextUtils.prefix + deliverTo.getName() + " was given a " + args[1] + " sapling");
        }
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.copyOf(FileManager.getSaplingsFile(plugin).getKeys(false));
        }
        return null;
    }
}
