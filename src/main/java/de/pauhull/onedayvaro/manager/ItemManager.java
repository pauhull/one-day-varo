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
    private ItemStack rename;

    @Getter
    private ItemStack backToLobby;

    @Getter
    private ItemStack inviteToTeam;

    public ItemManager() {
        this.backToLobby = new ItemBuilder().material(Material.SLIME_BALL).displayName("§cBack to lobby").build();
        this.rename = new ItemBuilder().material(Material.ANVIL).displayName("§dRename team").build();
        this.inviteToTeam = new ItemBuilder().material(Material.IRON_SWORD).displayName("§bInvite to team")
                .flag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).lore("§8» §7Left click: Invite player",
                        "§8» §7Right click: Team settings").unbreakable().build();
    }

    public void giveLobbyItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, inviteToTeam);
        player.getInventory().setItem(8, backToLobby);
    }

}
