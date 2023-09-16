package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.FileManager;
import me.jsinco.customsaplings.Saplings;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.Bukkit;
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
        if (sapling == null) {
            player.sendMessage(TextUtils.prefix + "That sapling does not exist!");
            return;
        }

        Player deliverTo = args.length == 3 ? Bukkit.getPlayerExact(args[2]) : player;
        if (deliverTo == null) {
            player.sendMessage(TextUtils.prefix + "That player is not online!");
            return;
        }

        for (int i = 0; i < 36; i++) {
            if (deliverTo.getInventory().getItem(i) == null || deliverTo.getInventory().getItem(i).equals(sapling)) {
                deliverTo.getInventory().addItem(sapling);
                break;
            } else if (i == 35) {
                deliverTo.getWorld().dropItem(player.getLocation(), sapling);
            }
        }

        if (!deliverTo.equals(player)) {
            player.sendMessage(TextUtils.prefix + deliverTo.getName() + " was given a: " + args[1] + " sapling");
        }
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return List.copyOf(FileManager.getSaplingsFile(plugin).getKeys(false));
    }
}
