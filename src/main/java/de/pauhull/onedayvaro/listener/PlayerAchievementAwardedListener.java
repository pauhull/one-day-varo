package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

/**
 * Created by Paul
 * on 05.04.2019
 *
 * @author pauhull
 */
public class PlayerAchievementAwardedListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerAchievementAwardedListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event) {

        event.setCancelled(true);
    }
}
