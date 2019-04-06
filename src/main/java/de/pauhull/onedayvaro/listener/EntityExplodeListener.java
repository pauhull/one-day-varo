package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Created by Paul
 * on 05.04.2019
 *
 * @author pauhull
 */
public class EntityExplodeListener implements Listener {

    private OneDayVaro oneDayVaro;

    public EntityExplodeListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        if (!oneDayVaro.isIngame()) {
            event.blockList().clear();
        }
    }

}
