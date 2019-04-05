package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Options.ProtectionPeriod;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Paul
 * on 02.04.2019
 *
 * @author pauhull
 */
public class ProtectionPeriodInventory implements Listener {

    private static final String TITLE = "§cSchutzzeit auswählen";
    private static final ItemStack TEAM_SIZE = new ItemBuilder().material(Material.PAPER).displayName("§eSchutzzeit: ").lore(" ", " §a§lKlicken zum bestätigen", " ").build();

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
                inventory.setItem(11 + i, new ItemBuilder(TEAM_SIZE).displayName("§eSchutzzeit:§f " + protectionPeriod.getName()).build());
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

            if (stack.getType() == TEAM_SIZE.getType()) {
                if (player.hasPermission(Permissions.Options)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    player.sendMessage(Locale.OptionChanged);
                    oneDayVaro.getOptions().setTeamSize(stack.getAmount());
                    oneDayVaro.getOptionsInventory().show(player);
                    Bukkit.broadcastMessage(Locale.TeamSizeChanged.replace("%SIZE%", Integer.toString(oneDayVaro.getOptions().getTeamSize())));
                }
            }
        }
    }
}