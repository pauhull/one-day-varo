package de.pauhull.onedayvaro.manager;

import de.pauhull.onedayvaro.util.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Paul
 * on 04.04.2019
 *
 * @author pauhull
 */
public class ItemManager {

    @Getter
    private ItemStack backToLobby;

    @Getter
    private ItemStack inviteToTeam;

    public ItemManager() {
        this.backToLobby = new ItemBuilder().material(Material.SLIME_BALL).displayName("§cZurück zur Lobby").build();
        this.inviteToTeam = new ItemBuilder()
                .material(Material.IRON_SWORD)
                .displayName("§bZu Team einladen")
                .flag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
                .lore("§8» §7Linksklick: Spieler einladen", "§8» §7Rechtsklick: Team-Optionen")
                .unbreakable()
                .build();
    }

    public void giveLobbyItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, inviteToTeam);
        player.getInventory().setItem(8, backToLobby);
    }

}
