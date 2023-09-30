package me.jsinco.customsaplings.listeners;

import me.jsinco.customsaplings.CustomSaplings;
import me.jsinco.customsaplings.Saplings;
import me.jsinco.customsaplings.util.TextUtils;
import me.jsinco.customsaplings.util.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;

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

        if (plugin.getConfig().getBoolean("require-permission-to-place") && !event.getPlayer().hasPermission("customsaplings.place")) {
            event.getPlayer().sendMessage(TextUtils.prefix + "You do not have permission to place this sapling!");
            event.setCancelled(true);
            return;
        }

        String schematic = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "schematic"), PersistentDataType.STRING);
        String sapling = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "sapling"), PersistentDataType.STRING);
        event.getBlockPlaced().setMetadata("schematic", new FixedMetadataValue(plugin, schematic));
        event.getBlockPlaced().setMetadata("sapling", new FixedMetadataValue(plugin, sapling));
        Util.log("&a" + event.getPlayer().getName() + " placed a " + sapling + " sapling!");
    }

    @EventHandler (ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        List<BlockState> blocks = event.getBlocks();

        for (BlockState block : blocks) {
            if (block.hasMetadata("schematic")) {
                Block cloneBlock = block.getBlock();
                List<MetadataValue> metadataValues = block.getMetadata("schematic");
                String schematic = metadataValues.get(0).asString();
                block.getWorld().getBlockAt(block.getLocation()).setType(Material.AIR);

                Saplings.setSchematic(schematic, cloneBlock);
                event.setCancelled(true);

                Util.log("&aA " + schematic + " tree has grown!");
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

        if (plugin.getConfig().getBoolean("require-permission-to-break") && !event.getPlayer().hasPermission("customsaplings.break")) {
            event.getPlayer().sendMessage(TextUtils.prefix + "You do not have permission to break this sapling!");
            Util.log("&e" + event.getPlayer().getName() + " tried to break a sapling but did not have permission! Location: " + event.getPlayer().getLocation());
            event.setCancelled(true);
            return;
        }

        List<MetadataValue> metadataValues = block.getMetadata("sapling");
        ItemStack sapling = Saplings.getSapling(metadataValues.get(0).asString());
        if (sapling != null) {
            block.getWorld().dropItemNaturally(block.getLocation(), sapling);
            Util.log("&a" + event.getPlayer().getName() + " broke a " + metadataValues.get(0).asString() + " sapling!");
        }
    }

    @EventHandler // For custom sapling boxes
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;


        if (event.getItem() == null || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "box"), PersistentDataType.STRING)) {
            return; // Not a custom sapling box
        }

        String rarity = event.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "box"), PersistentDataType.STRING);
        List<String> saplings = Saplings.getAllSaplingsOfRarity(rarity);
        if (saplings.isEmpty()) {
            event.getPlayer().sendMessage(TextUtils.prefix + "There are no saplings in this box!");
            Util.log("&eSomeone tried to open a box with no saplings in it! Rarity: " + rarity + " Location: " + event.getPlayer().getLocation());
        } else {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            String sapling = saplings.get(new Random().nextInt(saplings.size()));
            Util.giveItem(event.getPlayer(), Saplings.getSapling(sapling));
            Util.log("&a" + event.getPlayer().getName() + " opened a " + rarity + " box and got a " + sapling + " sapling!");

            if (plugin.getConfig().get("rarity-boxes." + rarity + ".open-sound") != null) {
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(plugin.getConfig().getString("rarity-boxes." + rarity + ".open-sound")), 1, 1);
            }
        }
    }
}
