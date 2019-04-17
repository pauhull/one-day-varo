package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public StatsCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        oneDayVaro.getStatsTable().getStats(player.getUniqueId(), stats -> {

            for (String line : Locale.Stats) {
                if (stats != null) {

                    int winRate = (int) ((stats.getWins() / (double) stats.getPlayedGames()) * 100);

                    line = line.replace("%PLAYER%", player.getName())
                            .replace("%KILLS%", Integer.toString(stats.getKills()))
                            .replace("%DEATHS%", Integer.toString(stats.getDeaths()))
                            .replace("%WINS%", Integer.toString(stats.getWins()))
                            .replace("%PLAYED_GAMES%", Integer.toString(stats.getPlayedGames()))
                            .replace("%KD%", stats.getDeaths() == 0 ? "NaN" : String.format("%.2f", stats.getKd()))
                            .replace("%WINRATE%", winRate + "%");
                } else {

                    line = line.replace("%PLAYER%", player.getName())
                            .replace("%KILLS%", "0")
                            .replace("%DEATHS%", "0")
                            .replace("%WINS%", "0")
                            .replace("%PLAYED_GAMES%", "0")
                            .replace("%KD%", "NaN")
                            .replace("%WINRATE%", "0%");
                }

                player.sendMessage(line);
            }
        });

        return true;
    }
}
