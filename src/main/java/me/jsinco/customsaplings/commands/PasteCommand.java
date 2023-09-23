package me.jsinco.customsaplings.commands;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.Saplings;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PasteCommand implements SubCommand  {

    @Override
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("customsaplings.pastecommand")) {
            sender.sendMessage(TextUtils.prefix + "You do not have permission to use this command!");
            return;
        }


        Block block = player.getTargetBlockExact(15);
        if (block == null) {
            player.sendMessage(TextUtils.prefix + "You must be looking at a block!");

        } else {
            Saplings.setSchematic(args[1], block);
            player.sendMessage(TextUtils.prefix + "Schematic set!");
        }
    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender sender, String[] args) {
        return null; // TODO: Make this return a list of schematic file names
    }
}
