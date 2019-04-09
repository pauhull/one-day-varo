package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
import de.pauhull.onedayvaro.util.Locale;
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
    private static final ItemStack SCENARIOS = new ItemBuilder().material(Material.MAGMA_CREAM).displayName("§9Szenarios").build();
    private static final ItemStack START = new ItemBuilder().material(Material.EMERALD).displayName("§aSpiel starten").build();
    private static final ItemStack NETHER = new ItemBuilder().material(Material.NETHERRACK).displayName("§cNether").build();
    private static final ItemStack PROTECTION_PERIOD = new ItemBuilder().material(Material.WATCH).displayName("§dSchutzzeit").build();
    private static final ItemStack WORLD = new ItemBuilder().material(Material.GRASS).displayName("§aWelt auswählen").build();
    private static final ItemStack BORDER = new ItemBuilder().material(Material.BARRIER).displayName("§9Border").build();

    private TeamSizeInventory teamSizeInventory;
    private ProtectionPeriodInventory protectionPeriodInventory;
    private WorldInventory worldInventory;
    private BorderInventory borderInventory;
    private ScenariosInventory scenariosInventory;
    private OneDayVaro oneDayVaro;

    public OptionsInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.teamSizeInventory = new TeamSizeInventory(oneDayVaro);
        this.protectionPeriodInventory = new ProtectionPeriodInventory(oneDayVaro);
        this.borderInventory = new BorderInventory(oneDayVaro);
        this.worldInventory = new WorldInventory(oneDayVaro);
        this.scenariosInventory = new ScenariosInventory(oneDayVaro);
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        ItemBuilder builder = new ItemBuilder(NETHER);
        if (oneDayVaro.getOptions().isNether()) {
            builder.lore(" ", " §a§lAktiviert", " ");
        } else {
            builder.lore(" ", " §c§lDeaktiviert", " ");
        }

        if (!oneDayVaro.isIngame()) {

            inventory.setItem(9, TEAM_SIZE);
            inventory.setItem(9 + 1, WORLD);
            inventory.setItem(9 + 2, SCENARIOS);
            inventory.setItem(9 + 4, START);
            inventory.setItem(9 + 6, builder.build());
            inventory.setItem(9 + 7, BORDER);
            inventory.setItem(9 + 8, PROTECTION_PERIOD);
        } else {

            inventory.setItem(9 + 4, SCENARIOS);
        }

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
                scenariosInventory.show(player);
            } else if (stack.equals(PROTECTION_PERIOD)) {
                protectionPeriodInventory.show(player);
            } else if (stack.equals(WORLD)) {
                worldInventory.show(player);
            } else if (stack.equals(BORDER)) {
                borderInventory.show(player);
            } else if (stack.getType() == NETHER.getType()) {
                player.sendMessage(Locale.OptionChanged);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                oneDayVaro.getOptions().setNether(!oneDayVaro.getOptions().isNether());
                ItemBuilder builder = new ItemBuilder(inventory.getItem(9 + 6));
                if (oneDayVaro.getOptions().isNether()) {
                    Bukkit.broadcastMessage(Locale.NetherEnabled);
                    builder.lore(" ", " §a§lAktiviert", " ");
                } else {
                    Bukkit.broadcastMessage(Locale.NetherDisabled);
                    builder.lore(" ", " §c§lDeaktiviert", " ");
                }
                inventory.setItem(9 + 6, builder.build());
            }
        }
    }

}
