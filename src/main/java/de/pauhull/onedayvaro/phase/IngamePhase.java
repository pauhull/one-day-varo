package de.pauhull.onedayvaro.phase;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Options.ProtectionPeriod;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Paul
 * on 05.04.2019
 *
 * @author pauhull
 */
public class IngamePhase implements Listener {

    private OneDayVaro oneDayVaro;

    @Getter
    private boolean canBuild;

    @Getter
    private boolean gracePeriod = true;

    public IngamePhase(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
    }

    public void start() {

        World world = oneDayVaro.getOptions().getWorld();

        if (world == null || oneDayVaro.isIngame()) {
            return;
        }

        oneDayVaro.setIngame(true);
        ArrayList<Location> spawns = oneDayVaro.getSpawnerManager().getSpawns(world);
        List<Player> teleportedPlayers = new ArrayList<>();
        int nextSpawnId = 0;

        for (Team team : Team.getAllTeams()) {
            for (Player player : team.getMembers()) {
                Location location = spawns.get(nextSpawnId++);

                if (location == null) {
                    player.kickPlayer(Locale.NoSpawnFound);
                    return;
                }

                player.teleport(location);
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                teleportedPlayers.add(player);
                player.getInventory().clear();
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (teleportedPlayers.contains(player)) {
                continue;
            }

            Location location = spawns.get(nextSpawnId++);

            if (location == null) {
                player.kickPlayer(Locale.NoSpawnFound);
                return;
            }

            player.teleport(location);
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            teleportedPlayers.add(player);
            player.getInventory().clear();
        }

        AtomicInteger task = new AtomicInteger();
        AtomicInteger countdown = new AtomicInteger(10);
        task.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(oneDayVaro, () -> {

            int i = countdown.getAndDecrement();

            if (i > 0) {

                if (i == 10 || i <= 5) {
                    Bukkit.broadcastMessage(Locale.GameStartsIn.replace("%TIME%", Integer.toString(i)));
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setExp(i / 10f);
                    player.setLevel(i);

                    if (i == 10 || i <= 5) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                }
            } else {

                Bukkit.broadcastMessage(Locale.GameStarted);
                canBuild = true;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                }

                this.gracePeriod();

                Bukkit.getScheduler().cancelTask(task.get());
            }
        }, 20L, 20L));
    }

    private void gracePeriod() {
        ProtectionPeriod protectionPeriod = oneDayVaro.getOptions().getProtectionPeriod();

        Bukkit.getScheduler().scheduleSyncDelayedTask(oneDayVaro, () -> {

            gracePeriod = false;
            Bukkit.broadcastMessage(Locale.GracePeriodEnd);

        }, 20 * protectionPeriod.getSeconds());
    }

}
