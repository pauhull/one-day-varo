package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class PlayerDropItemListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerDropItemListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        if (!this.oneDayVaro.isIngame()) {
            event.setCancelled(true);
        }
    }

}
