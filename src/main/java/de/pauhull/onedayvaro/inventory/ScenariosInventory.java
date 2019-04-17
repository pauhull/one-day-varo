package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.scenario.*;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul
 * on 18.02.2019
 *
 * @author pauhull
 */
public class ScenariosInventory implements Listener {

    private static final ItemStack ENABLED = new ItemBuilder().material(Material.INK_SACK).data(10).displayName("§aEnabled").build();
    private static final ItemStack DISABLED = new ItemBuilder().material(Material.INK_SACK).data(8).displayName("§7Disabled").build();
    private static final String TITLE = "§cScenarios";

    private Bowless bowless;
    private CutClean cutClean;
    private DoubleOres doubleOres;
    private Fireless fireless;
    private GoldenHead goldenHead;
    private NoFall noFall;
    private Rodless rodless;
    private Soup soup;
    private TimeBomb timeBomb;

    public ScenariosInventory(OneDayVaro oneDayVaro) {

        this.bowless = new Bowless(oneDayVaro);
        this.cutClean = new CutClean(oneDayVaro);
        this.doubleOres = new DoubleOres(oneDayVaro);
        this.fireless = new Fireless(oneDayVaro);
        this.goldenHead = new GoldenHead(oneDayVaro);
        this.noFall = new NoFall(oneDayVaro);
        this.rodless = new Rodless(oneDayVaro);
        this.soup = new Soup(oneDayVaro);
        this.timeBomb = new TimeBomb(oneDayVaro);
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);

        placeScenario(inventory, 0, bowless);
        placeScenario(inventory, 9, cutClean);
        placeScenario(inventory, 18, doubleOres);
        placeScenario(inventory, 27, fireless);
        placeScenario(inventory, 36, noFall);
        placeScenario(inventory, 45, rodless);
        placeScenario(inventory, 3, soup);
        placeScenario(inventory, 12, timeBomb);
        placeScenario(inventory, 21, goldenHead);

        /*
        placePerk(inventory, 0, Perk.NO_FIRE_DAMAGE, player);
        placePerk(inventory, 9, Perk.NO_WATER_DAMAGE, player);
        placePerk(inventory, 18, Perk.DOUBLE_JUMP, player);
        placePerk(inventory, 27, Perk.POTION_CLEAR, player);
        placePerk(inventory, 36, Perk.KEEP_XP, player);
        placePerk(inventory, 45, Perk.RUNNER, player);

        placePerk(inventory, 3, Perk.DOUBLE_XP, player);
        placePerk(inventory, 12, Perk.NO_HUNGER, player);
        placePerk(inventory, 21, Perk.ITEM_NAME, player);
        placePerk(inventory, 30, Perk.DROPPER, player);
        placePerk(inventory, 39, Perk.ARROW_POTION, player);
        placePerk(inventory, 48, Perk.NIGHT_VISION, player);

        placePerk(inventory, 6, Perk.ANTI_POISON, player);
        placePerk(inventory, 15, Perk.ANTI_FALL_DAMAGE, player);
        placePerk(inventory, 24, Perk.FAST_BREAK, player);
        placePerk(inventory, 33, Perk.MOB_SPAWNER, player);
        */

        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
        player.openInventory(inventory);
    }

    private void placeScenario(Inventory inventory, int slot, Scenario scenario) {

        List<String> lore = new ArrayList<>(scenario.getLore());
        lore.add(" ");
        lore.add(scenario.isEnabled() ? "§aEnabled" : "§cDisabled");
        ItemStack stack = new ItemBuilder()
                .material(scenario.getMaterial())
                .displayName(scenario.getDisplayName())
                .data(scenario.getDurability())
                .lore(lore)
                .flag(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        inventory.setItem(slot, stack);
        inventory.setItem(slot + 1, scenario.isEnabled() ? ENABLED : DISABLED);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack stack = event.getCurrentItem();

        if (inventory == null || inventory.getTitle() == null || !inventory.getTitle().equals(TITLE)) {
            return;
        } else {
            event.setCancelled(true);
        }

        if (stack == null || stack.getType() != Material.INK_SACK)
            return;

        ItemStack scenarioStack = inventory.getItem(event.getSlot() - 1);
        Scenario scenario = null;
        for (Scenario allScenarios : Scenario.getAllScenarios()) {
            if (allScenarios.getMaterial() == scenarioStack.getType() && allScenarios.getDurability() == scenarioStack.getDurability()
                    && (allScenarios.getDisplayName()).equals(scenarioStack.getItemMeta().getDisplayName())) {

                scenario = allScenarios;
            }
        }

        if (scenario == null)
            return;

        player.sendMessage(Locale.OptionChanged);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);

        scenario.setEnabled(!stack.equals(ENABLED));

        if (scenario.isEnabled()) {
            Bukkit.broadcastMessage(Locale.ScenarioEnabled.replace("%SCENARIO%", scenario.getDisplayName()));
        } else {
            Bukkit.broadcastMessage(Locale.ScenarioDisabled.replace("%SCENARIO%", scenario.getDisplayName()));
        }

        placeScenario(inventory, event.getSlot() - 1, scenario);
    }

}
