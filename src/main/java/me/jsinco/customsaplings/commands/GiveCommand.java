package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.FileManager;
import me.jsinco.customsaplings.Saplings;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiveCommand implements SubCommand {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("customsaplings.givecommand")) {
            sender.sendMessage("You do not have permission to use this command!");
            return;
        }
        if (args.length < 2) {
            player.sendMessage("You must specify a sapling name!");
            return;
        }

        ItemStack sapling = Saplings.getSapling(args[1]);
        if (sapling != null) {
            player.getInventory().addItem(sapling);
        } else {
            player.sendMessage(TextUtils.prefix + "That sapling does not exist!");
        }
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return List.copyOf(FileManager.getSaplingsFile(plugin).getKeys(false));
    }
}
