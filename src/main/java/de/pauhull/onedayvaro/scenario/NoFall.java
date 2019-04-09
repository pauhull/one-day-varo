package de.pauhull.onedayvaro.scenario;

import de.pauhull.onedayvaro.OneDayVaro;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collections;
import java.util.List;

public class NoFall extends Scenario implements Listener {

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

    public NoFall(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.lore = Collections.singletonList("§7Kein Fallschaden");
        this.material = Material.FEATHER;
        this.displayName = "§6NoFall";
        this.durability = 0;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
        allScenarios.add(this);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {

            event.setCancelled(true);
            event.setDamage(0);
        }
    }

}

