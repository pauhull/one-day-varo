package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerDeathListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        Player killer = player.getKiller();
        Team team = Team.getTeam(player);
        Team killerTeam = Team.getTeam(killer);

        StringBuilder stringBuilder = new StringBuilder(Locale.Prefix);
        stringBuilder.append("§e").append(player.getName()).append("§f ");
        if (team != null) {
            stringBuilder.append("aus dem Team §e").append(team.getName()).append(" §f");
        }
        if (killer != null) {
            stringBuilder.append("wurde von §e").append(killer.getName()).append("§f ");
            if (killerTeam != null) {
                stringBuilder.append("aus dem Team §e").append(killerTeam.getName()).append("§f ");
            }
            stringBuilder.append("getötet!");
        } else {
            stringBuilder.append("ist gestorben!");
        }
        event.setDeathMessage(null);
        Bukkit.broadcastMessage(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        if (killer == null) {

            stringBuilder.append("§fDu bist gestorben!");
        } else {

            stringBuilder.append("§fDu wurdest von §e").append(killer.getName()).append("§f ");
            if (killerTeam != null) {
                stringBuilder.append("aus dem Team §e").append(killerTeam.getName()).append("§f ");
            }
            stringBuilder.append("getötet!");
        }
        player.kickPlayer(stringBuilder.toString());

        oneDayVaro.getCoinsApi().removeCoins(player.getUniqueId().toString(), 25);
        player.sendMessage(Locale.CoinsRemoved.replace("%COINS%", "25"));

        if (killer != null) {
            oneDayVaro.getCoinsApi().addCoins(killer.getUniqueId().toString(), 100);
            killer.sendMessage(Locale.CoinsAdded.replace("%COINS%", "100"));
            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1, 1);
            int kills = 0;
            if (oneDayVaro.getIngamePhase().getKills().containsKey(killer.getName())) {
                kills = oneDayVaro.getIngamePhase().getKills().get(killer.getName());
            }
            oneDayVaro.getIngamePhase().getKills().put(killer.getName(), kills + 1);
            oneDayVaro.getScoreboardManager().updateTeam(killer);
            oneDayVaro.getScoreboardManager().updateTeam(player);

            oneDayVaro.getStatsTable().getStats(killer.getUniqueId(), stats -> {
                stats.setKills(stats.getKills() + 1);
                oneDayVaro.getStatsTable().insertStats(stats);
            });
        }

        oneDayVaro.getStatsTable().getStats(player.getUniqueId(), stats -> {
            stats.setDeaths(stats.getDeaths() + 1);
            oneDayVaro.getStatsTable().insertStats(stats);
        });

        oneDayVaro.getIngamePhase().checkForWin();
    }
}
