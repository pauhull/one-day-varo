package de.pauhull.onedayvaro.scenario;

import de.pauhull.onedayvaro.OneDayVaro;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Rodless extends Scenario implements Listener {

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

    public Rodless(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.lore = Collections.singletonList("§7No rods usable");
        this.material = Material.FISHING_ROD;
        this.displayName = "§aRodless";
        this.durability = 0;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
        allScenarios.add(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        ItemStack stack = event.getItem();

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (stack != null && stack.getType() == Material.FISHING_ROD) {
            event.setCancelled(true);
        }
    }
}

