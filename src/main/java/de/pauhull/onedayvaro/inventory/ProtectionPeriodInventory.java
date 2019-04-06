package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Options.ProtectionPeriod;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
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

/**
 * Created by Paul
 * on 02.04.2019
 *
 * @author pauhull
 */
public class ProtectionPeriodInventory implements Listener {

    private static final String TITLE = "§cSchutzzeit auswählen";
    private static final ItemStack PROTECTION_PERIOD = new ItemBuilder().material(Material.PAPER).displayName("§eSchutzzeit: ").lore(" ", " §5§lKlicken zum Auswählen", " ").build();

    private OneDayVaro oneDayVaro;

    public ProtectionPeriodInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        for (int i = 0; i < 5; i++) {
            ProtectionPeriod protectionPeriod = ProtectionPeriod.getById(i);

            if (protectionPeriod != null) {
                ItemBuilder builder = new ItemBuilder(PROTECTION_PERIOD);

                if (protectionPeriod == oneDayVaro.getOptions().getProtectionPeriod()) {
                    builder.material(Material.EMPTY_MAP).lore(" ", " §a§lAusgewählt", " ").enchant(Enchantment.DURABILITY, 10, true).flag(ItemFlag.HIDE_ENCHANTS);
                }

                builder.displayName("§eSchutzzeit:§f " + protectionPeriod.getName());
                inventory.setItem(11 + i, builder.build());
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

            if (stack.getType() == PROTECTION_PERIOD.getType()) {
                if (player.hasPermission(Permissions.Options)) {

                    ProtectionPeriod protectionPeriod = ProtectionPeriod.getById(event.getRawSlot() - 11);

                    if (protectionPeriod != null) {

                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                        player.sendMessage(Locale.OptionChanged);
                        oneDayVaro.getOptions().setProtectionPeriod(protectionPeriod);
                        oneDayVaro.getOptionsInventory().show(player);
                        Bukkit.broadcastMessage(Locale.ProtectionPeriodChanged.replace("%PERIOD%", protectionPeriod.getName()));
                    }
                }
            }
        }
    }
}