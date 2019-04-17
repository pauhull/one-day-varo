package de.pauhull.onedayvaro.inventory;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
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
 * on 06.04.2019
 *
 * @author pauhull
 */
public class TeamInventory implements Listener {

    private static final String TITLE = "§cTeam settings";
    private static final ItemStack DELETE_TEAM = new ItemBuilder().material(Material.BARRIER).displayName("§4Delete team").build();
    private static final ItemStack LEAVE = new ItemBuilder().material(Material.WOOD_DOOR).displayName("§cLeave team").build();

    private OneDayVaro oneDayVaro;

    public TeamInventory(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    public void show(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 9, TITLE);

        Team team = Team.getTeam(player);

        if (team == null) {
            player.sendMessage(Locale.NoTeam);
            return;
        }

        if (team.getOwner() == player) {
            inventory.setItem(4, DELETE_TEAM);
        } else {
            inventory.setItem(4, LEAVE);
        }

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        Team team = Team.getTeam(player);
        Inventory inventory = event.getInventory();
        ItemStack stack = event.getCurrentItem();

        if (inventory == null || inventory.getTitle() == null || !inventory.getTitle().equals(TITLE)) {
            return;
        } else {
            event.setCancelled(true);
        }


        if (stack != null && team != null) {

            if (stack.equals(DELETE_TEAM)) {

                if (player == team.getOwner()) {
                    team.broadcast(Locale.TeamDeleted);
                    team.delete();
                }

                player.getInventory().setItem(1, null);

                player.closeInventory();
            } else if (stack.equals(LEAVE)) {

                if (player != team.getOwner()) {
                    team.broadcast(Locale.LeftTeam.replace("%PLAYER%", player.getName()));
                    team.getMembers().remove(player);
                }

                player.closeInventory();
            }
        }
    }

}
