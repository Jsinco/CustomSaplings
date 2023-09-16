package me.jsinco.customsaplings.commands;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.FileManager;
import me.jsinco.customsaplings.Schematic;
import me.jsinco.customsaplings.util.TextUtils;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class PasteCommand implements SubCommand  {

    @Override // TODO: cleanup
    public void execute(CustomSaplings plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player) || !player.hasPermission("customsaplings.pastecommand")) {
            sender.sendMessage("You do not have permission to use this command!");
            return;
        }

        File file = FileManager.getSchematicFile(plugin, args[1]);
        Block block = player.getTargetBlockExact(15);
        if (file == null || !file.exists()) {
            player.sendMessage(TextUtils.prefix + "That schematic does not exist!");
            return;
        } else if (block == null) {
            player.sendMessage(TextUtils.prefix + "You must be looking at a block!");
            return;
        }
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();
            Schematic schematic = new Schematic(clipboard);

            schematic.paste(block.getLocation());
            player.sendMessage(TextUtils.prefix + "Pasted the schematic!");
        } catch (IOException e) {
            player.sendMessage(TextUtils.prefix + "An error occurred while pasting the schematic!");
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<String> tabComplete(CustomSaplings plugin, CommandSender commandSender, String[] args) {
        return null; // TODO: Make this return a list of schematic file names
    }
}
