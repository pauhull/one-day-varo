package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Paul
 * on 02.04.2019
 *
 * @author pauhull
 */
public class TeamSizeInventory implements Listener {

    private static final String TITLE = "§cTeamgröße auswählen";
    private static final ItemStack TEAM_SIZE = new ItemBuilder().material(Material.IRON_CHESTPLATE).displayName("§eTeamgröße: §f1").lore(" ", " §a§lKlicken zum bestätigen", " ").build();
    private static final ItemStack INCREASE = new ItemBuilder().material(Material.WOOD_BUTTON).displayName("§7Um 1 erhöhen").build();
    private static final ItemStack DECREASE = new ItemBuilder().material(Material.WOOD_BUTTON).displayName("§7Um 1 verkleinern").build();
    private static final ItemStack NOT_AVAILABLE = new ItemBuilder().material(Material.STONE_BUTTON).displayName("§cNicht verfügbar").build();

    private OneDayVaro oneDayVaro;

    public TeamSizeInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        ItemStack teamSizeStack = new ItemBuilder(TEAM_SIZE).amount(oneDayVaro.getOptions().getTeamSize()).build();
        inventory.setItem(4, NOT_AVAILABLE);
        inventory.setItem(9 + 4, teamSizeStack);
        inventory.setItem(2 * 9 + 4, NOT_AVAILABLE);
        changeSize(inventory, 0);

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

            if (stack.equals(INCREASE)) {
                changeSize(inventory, 1);
            } else if (stack.equals(DECREASE)) {
                changeSize(inventory, -1);
            } else if (stack.getType() == TEAM_SIZE.getType()) {

                if (player.hasPermission(Permissions.Options)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    player.sendMessage(Locale.OptionChanged);
                    oneDayVaro.getOptions().setTeamSize(inventory.getItem(4 + 9).getAmount());
                    oneDayVaro.getOptionsInventory().show(player);
                }
            }
        }
    }

    private void changeSize(Inventory inventory, int factor) {

        int current = inventory.getItem(9 + 4).getAmount();

        if (current + factor < 1 || current + factor > 10) {
            return;
        }

        if (current + factor == 1) {
            inventory.setItem(2 * 9 + 4, NOT_AVAILABLE);
        } else if (NOT_AVAILABLE.equals(inventory.getItem(2 * 9 + 4))) {
            inventory.setItem(2 * 9 + 4, DECREASE);
        }

        if (current + factor == 10) {
            inventory.setItem(4, NOT_AVAILABLE);
        } else if (NOT_AVAILABLE.equals(inventory.getItem(4))) {
            inventory.setItem(4, INCREASE);
        }

        int newAmount = current + factor;
        ItemStack stack = inventory.getItem(9 + 4);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("§eTeamgröße: §f" + newAmount);
        stack.setItemMeta(meta);
        stack.setAmount(newAmount);
        inventory.setItem(9 + 4, stack);

        for (HumanEntity viewer : inventory.getViewers()) {
            ((Player) viewer).playSound(viewer.getLocation(), Sound.CLICK, 1, 1);
        }
    }
}