package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class PlayerQuitListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerQuitListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        oneDayVaro.getIngamePhase().getSpectators().remove(player);

        if (oneDayVaro.isIngame() && !oneDayVaro.getIngamePhase().isCanBuild() && Bukkit.getOnlinePlayers().size() - 1 == 0) {
            Bukkit.getServer().spigot().restart();
            return;
        }

        if (oneDayVaro.isIngame()) {
            event.setQuitMessage(null);

            Bukkit.getScheduler().scheduleSyncDelayedTask(oneDayVaro, () -> oneDayVaro.getIngamePhase().checkForWin(), 0);

            if (!oneDayVaro.getIngamePhase().getSpectators().contains(player)) {
                oneDayVaro.getCoinsApi().removeCoins(player.getUniqueId().toString(), 25);
                player.sendMessage(Locale.CoinsRemoved.replace("%COINS%", "25"));
            }

        } else {
            event.setQuitMessage(Locale.LobbyLeave.replace("%PLAYER%", player.getName()));
        }

        for (Team team : Team.getAllTeams()) {
            team.getInvited().remove(player);
        }

        Team team = Team.getTeam(player);

        if (team != null) {
            if (team.getOwner() == player) {
                team.broadcast(Locale.TeamDeleted);
                team.delete();
            } else {
                team.broadcast(Locale.LeftTeam.replace("%PLAYER%", player.getName()));
                team.getMembers().remove(player);
            }
        }
    }

}
