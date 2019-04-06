package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Paul
 * on 06.04.2019
 *
 * @author pauhull
 */
public class WorldInventory implements Listener {

    private static final ItemStack RANDOM = new ItemBuilder().material(Material.FEATHER).displayName("§bZufällig").build();
    private static final ItemStack NO_WORLDS = new ItemBuilder().material(Material.BARRIER).displayName("§4Keine Welten verfügbar").build();

    private static final String TITLE = "§cWelten";

    private OneDayVaro oneDayVaro;

    public WorldInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        List<World> worlds = oneDayVaro.getSpawnerManager().getWorlds();
        int slots;

        if (worlds.isEmpty()) {
            slots = 9;
        } else {
            slots = (int) Math.ceil((double) (worlds.size() + 1) / 9.0) * 9;
        }

        Inventory inventory = Bukkit.createInventory(null, slots, TITLE);

        if (worlds.isEmpty()) {

            inventory.setItem(0, NO_WORLDS);
        } else {

            ItemBuilder randomBuilder = new ItemBuilder(RANDOM);
            if (oneDayVaro.getOptions().getWorld() == null) {

                randomBuilder.lore(" ", " §a§lAusgewählt", " ")
                        .enchant(Enchantment.DURABILITY, 10, true)
                        .flag(ItemFlag.HIDE_ENCHANTS);
            } else {

                randomBuilder.lore(" ", "§5§l Klicken zum Auswählen", " ");
            }
            inventory.setItem(0, randomBuilder.build());

            int slot = 1;

            for (World world : worlds) {

                ItemBuilder worldBuilder = new ItemBuilder();

                if (oneDayVaro.getOptions().getWorld() == world) {

                    worldBuilder.material(Material.EMPTY_MAP)
                            .enchant(Enchantment.DURABILITY, 10, true)
                            .flag(ItemFlag.HIDE_ENCHANTS)
                            .lore(" ", " §a§lAusgewählt", " ");
                } else {

                    worldBuilder.material(Material.PAPER)
                            .lore(" ", " §5§lKlicken zum Auswählen", " ");
                }

                worldBuilder.displayName("§eWelt: §f" + world.getName());
                inventory.setItem(slot++, worldBuilder.build());
            }
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
            if (!stack.getEnchantments().isEmpty() || stack.equals(NO_WORLDS)) {
                return;
            }

            if (stack.getType() == RANDOM.getType()) {

                oneDayVaro.getOptions().setWorld(null);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                player.sendMessage(Locale.OptionChanged);
                oneDayVaro.getOptionsInventory().show(player);
                Bukkit.broadcastMessage(Locale.WorldChanged.replace("%WORLD%", "Zufällig"));
            } else {

                World world = Bukkit.getWorld(ChatColor.stripColor(stack.getItemMeta().getDisplayName()).replace("Welt: ", ""));
                oneDayVaro.getOptions().setWorld(world);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                player.sendMessage(Locale.OptionChanged);
                oneDayVaro.getOptionsInventory().show(player);
                Bukkit.broadcastMessage(Locale.WorldChanged.replace("%WORLD%", world.getName()));
            }
        }
    }

}
