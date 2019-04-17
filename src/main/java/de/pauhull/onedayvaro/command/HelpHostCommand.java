package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpHostCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public HelpHostCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("helphost").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.HelpHost)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Locale.Prefix + "§c/helphost <Nachricht...>");
            return true;
        }

        StringBuilder builder = new StringBuilder();
        for (String arg : args) {

            if (builder.length() > 0) {
                builder.append(" ");
            }

            builder.append(arg);
        }
        String message = builder.toString();

        List<CommandSender> receivers = new ArrayList<>();
        receivers.add(sender);
        Player host = Bukkit.getPlayer(oneDayVaro.getHost());
        if (host != null && !receivers.contains(host)) receivers.add(host);

        receivers.forEach(s -> s.sendMessage(Locale.HelpHost.replace("%PLAYER%", sender.getName())
                .replace("%MESSAGE%", message).replace(Locale.Prefix, "")));

        return true;
    }
}
