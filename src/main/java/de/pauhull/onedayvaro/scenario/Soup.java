package de.pauhull.onedayvaro.scenario;

import de.pauhull.onedayvaro.OneDayVaro;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Soup extends Scenario implements Listener {

    @Getter
    private String displayName;

    @Getter
    private Material material;

    @Getter
    private short durability;

    @Getter
    @Setter
    private boolean enabled;

    @Getter
    private List<String> lore;

    private OneDayVaro oneDayVaro;

    public Soup(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.lore = Collections.singletonList("§7Soups heal instantly");
        this.material = Material.MUSHROOM_SOUP;
        this.displayName = "§eSoup";
        this.durability = 0;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
        allScenarios.add(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack stack = event.getItem();

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (stack != null && stack.getType() == Material.MUSHROOM_SOUP) {

            event.setCancelled(true);
            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 6));
            player.getInventory().remove(new ItemStack(Material.MUSHROOM_SOUP));
        }
    }
}

