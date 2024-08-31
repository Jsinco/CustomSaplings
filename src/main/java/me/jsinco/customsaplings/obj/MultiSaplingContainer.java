package me.jsinco.customsaplings.obj;

import me.jsinco.customsaplings.util.Util;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MultiSaplingContainer implements SaplingContainer {

    private final Material material;
    private final String name;
    private final List<String> lore;
    private final boolean enchantGlint;
    private final int customModelData;
    private final String rarityBox;

    private final String id;
    private final List<String> schematics;

    public MultiSaplingContainer(Material material, String name, List<String> lore, boolean enchantGlint, int customModelData, String rarityBox, String id, List<String> schematics) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.enchantGlint = enchantGlint;
        this.customModelData = customModelData;
        this.rarityBox = rarityBox;
        this.id = id;
        this.schematics = schematics;
    }


    @Override
    public Material material() {
        return material;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<String> lore() {
        return lore;
    }

    @Override
    public boolean enchantGlint() {
        return enchantGlint;
    }

    @Override
    public int customModelData() {
        return customModelData;
    }

    @Override
    public String rarityBox() {
        return rarityBox;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSchematic() {
        return Util.getRand(schematics);
    }

    public static class Builder {
        private Material material = Material.OAK_SAPLING;
        private String name = "Unnamed Sapling";
        private List<String> lore = new ArrayList<>();
        private boolean enchantGlint = false;
        private int customModelData = 0;
        private String rarityBox;
        private String id;
        private List<String> schematics;

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public Builder enchantGlint(boolean enchantGlint) {
            this.enchantGlint = enchantGlint;
            return this;
        }

        public Builder customModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        public Builder rarityBox(String rarityBox) {
            this.rarityBox = rarityBox;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder schematics(List<String> schematics) {
            this.schematics = schematics;
            return this;
        }

        public MultiSaplingContainer build() {
            return new MultiSaplingContainer(material, name, lore, enchantGlint, customModelData, rarityBox, id, schematics);
        }
    }
}
