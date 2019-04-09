package de.pauhull.onedayvaro.phase;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.display.IngameScoreboard;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.ChatFace;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Options.ProtectionPeriod;
import de.pauhull.onedayvaro.util.Title;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Created by Paul
 * on 05.04.2019
 *
 * @author pauhull
 */
public class IngamePhase implements Listener {

    private OneDayVaro oneDayVaro;
    private Random random;
    private List<ItemStack> itemStacks;
    private boolean ended;

    @Getter
    private List<Player> spectators;

    @Getter
    private boolean canBuild;

    @Getter
    private boolean gracePeriod = true;

    @Getter
    private Map<String, Integer> kills;

    public IngamePhase(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.ended = false;
        this.random = new Random();
        this.itemStacks = new ArrayList<>();
        this.kills = new HashMap<>();
        this.spectators = new ArrayList<>();

        for (String materialString : oneDayVaro.getConfig().getConfigurationSection("StarterItems").getKeys(false)) {

            Material material;
            try {
                material = Material.valueOf(materialString.toUpperCase());
            } catch (IllegalArgumentException ex) {
                Bukkit.getConsoleSender().sendMessage("§c[ODVPlugin] Couldn't find material \"" + materialString.toUpperCase() + "\"");
                continue;
            }

            int amount = oneDayVaro.getConfig().getInt("StarterItems." + materialString);

            itemStacks.add(new ItemStack(material, amount));
        }
    }

    public void start() {

        List<World> worlds = oneDayVaro.getSpawnerManager().getWorlds();

        if (worlds.isEmpty() || oneDayVaro.isIngame()) {
            return;
        }

        World world = oneDayVaro.getOptions().getWorld();

        if (world == null) {
            world = worlds.get(random.nextInt(worlds.size()));
        }

        oneDayVaro.getScoreboardManager().setScoreboard(IngameScoreboard.class);

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
                player.closeInventory();
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
            player.closeInventory();
        }

        AtomicInteger task = new AtomicInteger();
        AtomicInteger countdown = new AtomicInteger(60);
        task.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(oneDayVaro, () -> {

            int i = countdown.getAndDecrement();

            if (i == 60 || i == 30 || i == 10) {
                Bukkit.broadcastMessage(Locale.GameStartsIn.replace("%TIME%", Integer.toString(i)));
            }

            if (i > 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {

                    player.setExp(i / 60f);
                    player.setLevel(i);

                    if (i <= 10) {
                        Title.playTitle(player, " ", Locale.GameStartsIn.replace("%TIME%", Integer.toString(i)).replace(Locale.Prefix, ""), 0, 20, 10);
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
                    }
                }
            } else {

                Bukkit.broadcastMessage(Locale.GameStarted);
                canBuild = true;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                    player.getInventory().addItem(itemStacks.toArray(new ItemStack[0]));
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setExp(0);
                    player.setLevel(0);

                    oneDayVaro.getStatsTable().getStats(player.getUniqueId(), stats -> {
                        stats.setPlayedGames(stats.getPlayedGames() + 1);
                        oneDayVaro.getStatsTable().insertStats(stats);
                    });
                }

                this.gracePeriod();

                Bukkit.getScheduler().cancelTask(task.get());
            }
        }, 20L, 20L));
    }

    private void gracePeriod() {
        ProtectionPeriod protectionPeriod = oneDayVaro.getOptions().getProtectionPeriod();

        if (protectionPeriod.getSeconds() > 0) {
            Bukkit.broadcastMessage(Locale.GracePeriodEndsIn.replace("%PERIOD%", protectionPeriod.getName()));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(oneDayVaro, () -> {

            gracePeriod = false;
            Bukkit.broadcastMessage(Locale.GracePeriodEnd);

        }, 20 * protectionPeriod.getSeconds());
    }

    public void checkForWin() {

        Player winner = null;
        int differentTeams = 0;
        List<Team> countedTeams = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {

            winner = player;

            if (spectators.contains(player)) {
                continue;
            }

            Team team = Team.getTeam(player);

            if (team == null) {

                differentTeams++;
            } else {

                if (!countedTeams.contains(team)) {
                    countedTeams.add(team);
                    differentTeams++;
                }
            }

        }

        if (differentTeams != 1 || winner == null || ended) {
            return;
        }

        ended = true;

        Bukkit.broadcastMessage(" ");

        Team winningTeam = Team.getTeam(winner);

        if (winningTeam == null) {

            String[] lines = ChatFace.getLines(winner.getUniqueId().toString());
            Arrays.asList(lines).forEach(Bukkit::broadcastMessage);
            Bukkit.broadcastMessage(Locale.PlayerWonGame.replace("%PLAYER%", winner.getName()));

            for (Player player : Bukkit.getOnlinePlayers()) {
                Title.playTitle(player, "", Locale.PlayerWonGame.replace("%PLAYER%", winner.getName())
                        .replace(Locale.Prefix, ""), 0, 60, 20);
            }
        } else {

            Bukkit.broadcastMessage(Locale.TeamWonGame.replace("%TEAM%", winningTeam.getName()));

            for (Player player : Bukkit.getOnlinePlayers()) {
                Title.playTitle(player, "", Locale.TeamWonGame.replace("%TEAM%", winningTeam.getName())
                        .replace(Locale.Prefix, ""), 0, 60, 20);
            }
        }

        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(Locale.TopKills);

        Stream<Map.Entry<String, Integer>> sorted = kills.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue));
        sorted.forEach(entry -> Bukkit.broadcastMessage(" §6" + entry.getKey() + "§8: §e" + entry.getValue()));

        if (winningTeam != null) {

            winningTeam.getMembers().forEach(player -> oneDayVaro.getStatsTable().getStats(player.getUniqueId(), stats -> {
                stats.setWins(stats.getWins() + 1);
                oneDayVaro.getStatsTable().insertStats(stats);
            }));
        } else {

            oneDayVaro.getStatsTable().getStats(winner.getUniqueId(), stats -> {
                stats.setWins(stats.getWins() + 1);
                oneDayVaro.getStatsTable().insertStats(stats);
            });
        }


        AtomicInteger task = new AtomicInteger();
        AtomicInteger countdown = new AtomicInteger(30);
        Bukkit.broadcastMessage(Locale.ServerRestartsIn.replace("%TIME%", Integer.toString(countdown.get())));
        task.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(oneDayVaro, () -> {

            int i = countdown.getAndDecrement();

            if (i <= 10 && i > 0) {
                Bukkit.broadcastMessage(Locale.ServerRestartsIn.replace("%TIME%", Integer.toString(i)));
            }

            if (i == 0) {
                Bukkit.getScheduler().cancelTask(task.get());
                Bukkit.getServer().spigot().restart();
            }

        }, 20L, 20L));
    }
}
