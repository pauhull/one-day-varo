package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
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
public class OptionsInventory implements Listener {

    private static final String TITLE = "§cOptionen";
    private static final ItemStack TEAM_SIZE = new ItemBuilder().material(Material.IRON_CHESTPLATE).displayName("§bTeamgröße ändern").build();
    private static final ItemStack SCENARIOS = new ItemBuilder().material(Material.MAGMA_CREAM).displayName("§cSzenarios").build();
    private static final ItemStack START = new ItemBuilder().material(Material.EMERALD).displayName("§aSpiel starten").build();
    private static final ItemStack NETHER = new ItemBuilder().material(Material.NETHERRACK).displayName("§cNether an/aus").build();

    private TeamSizeInventory teamSizeInventory;
    private OneDayVaro oneDayVaro;

    public OptionsInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.teamSizeInventory = new TeamSizeInventory(oneDayVaro);
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        inventory.setItem(9 + 1, TEAM_SIZE);
        inventory.setItem(9 + 3, START);
        inventory.setItem(9 + 5, SCENARIOS);
        inventory.setItem(9 + 7, NETHER);

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
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
            if (stack.equals(TEAM_SIZE)) {
                teamSizeInventory.show(player);
            } else if (stack.equals(START)) {
                Bukkit.dispatchCommand(player, "start");
            } else if (stack.equals(SCENARIOS)) {

            }
        }
    }

}
