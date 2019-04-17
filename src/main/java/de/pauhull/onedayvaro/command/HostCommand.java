package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HostCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public HostCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("host").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.SetHost)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Locale.Prefix + "§c/sethost <Player>");
            return true;
        }

        Player oldHost = Bukkit.getPlayer(oneDayVaro.getHost());

        String host = args[0];
        Player player = Bukkit.getPlayer(host);

        if (player != null) {
            host = player.getName();
        }

        oneDayVaro.setHost(host);
        Bukkit.broadcastMessage(Locale.HostSet.replace("%HOST%", host));

        if (player != null) {
            oneDayVaro.getScoreboardManager().updateTeam(player);
        }

        if (oldHost != null) {
            oneDayVaro.getScoreboardManager().updateTeam(oldHost);
        }

        return true;
    }
}
