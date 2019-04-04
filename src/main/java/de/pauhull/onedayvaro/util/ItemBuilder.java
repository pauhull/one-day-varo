package de.pauhull.onedayvaro.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class ItemBuilder {

    private ItemStack stack;

    public ItemBuilder() {
        this(null);
    }

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemBuilder material(Material material) {
        stack = new ItemStack(material);
        return this;
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder data(int durability) {
        stack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder displayName(String displayName) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return stack;
    }

}
