package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class EntityDamageListener implements Listener {

    private OneDayVaro oneDayVaro;

    public EntityDamageListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if (!oneDayVaro.isIngame()) {
            event.setCancelled(true);
            event.setDamage(0);
        }
    }

}
