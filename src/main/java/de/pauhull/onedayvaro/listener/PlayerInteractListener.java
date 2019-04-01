package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class PlayerInteractListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerInteractListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!this.oneDayVaro.isIngame()) {
            event.setCancelled(true);
        }
    }

}
