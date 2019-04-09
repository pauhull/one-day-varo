package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.ItemBuilder;
import de.pauhull.onedayvaro.util.Locale;
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
public class TeamSizeInventory implements Listener {

    private static final String TITLE = "§cTeamgröße auswählen";
    private static final ItemStack TEAM_SIZE = new ItemBuilder().material(Material.PAPER).displayName("§eTeamgröße: ").lore(" ", " §5§lKlicken zum Auswählen", " ").build();

    private OneDayVaro oneDayVaro;

    public TeamSizeInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        for (int i = 0; i < 5; i++) {
            ItemBuilder builder = new ItemBuilder(TEAM_SIZE);

            if (i + 1 == oneDayVaro.getOptions().getTeamSize()) {
                builder.material(Material.EMPTY_MAP).lore(" ", " §a§lAusgewählt", " ").enchant(Enchantment.DURABILITY, 10, true).flag(ItemFlag.HIDE_ENCHANTS);
            }

            builder.displayName("§eTeamgröße:§f " + (i + 1)).amount(i + 1);
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

            if (stack.getType() == TEAM_SIZE.getType()) {
                if (player.hasPermission(Permissions.Options)) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    player.sendMessage(Locale.OptionChanged);
                    oneDayVaro.getOptions().setTeamSize(stack.getAmount());
                    oneDayVaro.getOptionsInventory().show(player);
                    Bukkit.broadcastMessage(Locale.TeamSizeChanged.replace("%SIZE%", Integer.toString(oneDayVaro.getOptions().getTeamSize())));

                    if (oneDayVaro.getOptions().getTeamSize() == 1) {
                        Team.getAllTeams().forEach(Team::delete);
                    } else if (oneDayVaro.getOptions().getTeamSize() < 5) {

                        for (Team team : Team.getAllTeams()) {
                            while (team.getMembers().size() > oneDayVaro.getOptions().getTeamSize()) {
                                team.getMembers().remove(team.getMembers().get(team.getMembers().size() - 1));
                            }
                        }
                    }
                }
            }
        }
    }
}