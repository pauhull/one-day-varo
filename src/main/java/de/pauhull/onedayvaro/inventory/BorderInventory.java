package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class BorderInventory implements Listener {

    private static final String TITLE = "§cBorder";

    private OneDayVaro oneDayVaro;
    private int[] possibleBorders;

    public BorderInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.possibleBorders = new int[]{500, 1000, 1500, 2000, 2500};
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        for (int i = 0; i < possibleBorders.length; i++) {

            int border = possibleBorders[i];

            ItemBuilder builder = new ItemBuilder();
            if (oneDayVaro.getOptions().getBorder() == border) {
                builder.material(Material.EMPTY_MAP)
                        .enchant(Enchantment.DURABILITY, 10, true)
                        .flag(ItemFlag.HIDE_ENCHANTS)
                        .lore(" ", " §a§lAusgewählt", " ");
            } else {
                builder.material(Material.PAPER)
                        .lore(" ", " §5§lKlicken zum Auswählen", " ");
            }
            builder.displayName("§eBorder: §f" + border + " Blöcke");
            inventory.setItem(11 + i, builder.build());
        }

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack stack = event.getCurrentItem();

        if (inventory != null && inventory.getTitle() != null && inventory.getTitle().equals(TITLE)) {
            event.setCancelled(true);
        } else {
            return;
        }

        if (stack != null) {
            if (stack.getType() == Material.PAPER) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                player.sendMessage(Locale.OptionChanged);
                oneDayVaro.getOptions().setBorder(Integer.valueOf(ChatColor.stripColor(stack.getItemMeta().getDisplayName()).replace("Border: ", "").replace(" Blöcke", "")));
                oneDayVaro.getOptionsInventory().show(player);
                Bukkit.broadcastMessage(Locale.BorderChanged.replace("%BORDER%", Integer.toString(oneDayVaro.getOptions().getBorder())));
            }
        }
    }
}
