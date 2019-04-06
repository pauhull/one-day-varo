package de.pauhull.onedayvaro.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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
    private ItemMeta meta;

    public ItemBuilder() {
    }

    public ItemBuilder(ItemStack stack) {

        this.stack = stack == null ? null : stack.clone();
        this.meta = stack == null ? null : stack.getItemMeta();
    }

    public ItemBuilder material(Material material) {

        this.stack = new ItemStack(material);
        this.meta = stack.getItemMeta();
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

        meta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder lore(String... lore) {

        meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level, boolean unsafe) {

        meta.addEnchant(enchantment, level, unsafe);
        return this;
    }

    public ItemBuilder flag(ItemFlag... flags) {

        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder unbreakable() {

        meta.spigot().setUnbreakable(true);
        return this;
    }

    public ItemStack build() {

        stack.setItemMeta(meta);
        return stack;
    }

}
