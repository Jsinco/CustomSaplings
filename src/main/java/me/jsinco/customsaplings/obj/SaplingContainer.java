package me.jsinco.customsaplings.obj;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SaplingContainer {

    Material material();
    String name();
    List<String> lore();
    boolean enchantGlint();
    int customModelData();
    @Nullable
    String rarityBox();

    String getId();
    String getSchematic();
}
