package me.jsinco.customsaplings.listeners;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.FileManager;
import me.jsinco.customsaplings.Saplings;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.List;

public class Events implements Listener {

    private final CustomSaplings plugin;

    public Events(CustomSaplings plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();

        if (!item.hasItemMeta() || !item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "schematic"), PersistentDataType.STRING)) {
            return; // Not a custom sapling
        }
        String schematic = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "schematic"), PersistentDataType.STRING);
        String sapling = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "sapling"), PersistentDataType.STRING);
        event.getBlockPlaced().setMetadata("schematic", new FixedMetadataValue(plugin, schematic));
        event.getBlockPlaced().setMetadata("sapling", new FixedMetadataValue(plugin, sapling));
    }

    @EventHandler (ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        List<BlockState> blocks = event.getBlocks();

        for (BlockState block : blocks) {
            if (block.hasMetadata("schematic")) {
                List<MetadataValue> metadataValues = block.getMetadata("schematic");
                String schematic = metadataValues.get(0).asString();
                Saplings.setTree(schematic, block.getBlock());

                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.hasMetadata("sapling")) {
            return; // Not a custom sapling
        } else if (!plugin.getConfig().getBoolean("drop-sapling-item-on-break")) {
            return; // Drop sapling is disabled
        }

        List<MetadataValue> metadataValues = block.getMetadata("sapling");
        ItemStack sapling = Saplings.getSapling(metadataValues.get(0).asString());
        if (sapling != null) {
            block.getWorld().dropItemNaturally(block.getLocation(), sapling);
        }
    }
}
