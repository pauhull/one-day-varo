package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Paul
 * on 02.04.2019
 *
 * @author pauhull
 */
public class StartCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public StartCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("start").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.Start) && !sender.getName().equalsIgnoreCase(oneDayVaro.getHost())) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (oneDayVaro.getSpawnerManager().getWorlds().isEmpty()) {
            sender.sendMessage(Locale.NotAnySpawns);
            return true;
        }

        if (Bukkit.getOnlinePlayers().size() < oneDayVaro.getOptions().getTeamSize() + 1) {
            sender.sendMessage(Locale.NotEnoughPlayers);
            return true;
        }

        oneDayVaro.getIngamePhase().start();

        return true;
    }

}
