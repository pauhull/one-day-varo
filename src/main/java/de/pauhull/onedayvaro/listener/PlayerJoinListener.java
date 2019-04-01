package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class PlayerJoinListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerJoinListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (oneDayVaro.isIngame()) {
            event.setJoinMessage(null);
            oneDayVaro.getLocationManager().teleport("Spectator", player);
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            event.setJoinMessage(Locale.LobbyJoin.replace("%PLAYER%", player.getName()));
            oneDayVaro.getLocationManager().teleport("Lobby", player);
        }
    }

}