package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class FoodLevelChangeListener implements Listener {

    private OneDayVaro oneDayVaro;

    public FoodLevelChangeListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if (!oneDayVaro.getIngamePhase().isCanBuild()) {
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }

}
