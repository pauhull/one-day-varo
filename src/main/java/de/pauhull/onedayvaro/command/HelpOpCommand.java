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

public class HelpOpCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public HelpOpCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("helpop").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.HelpOp)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Locale.Prefix + "§c/helpop <Message...>");
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(Permissions.HelpOpReceive) && !receivers.contains(player)) {
                receivers.add(player);
            }
        }

        receivers.forEach(s -> s.sendMessage(Locale.HelpOp.replace("%PLAYER%", sender.getName())
                .replace("%MESSAGE%", message).replace(Locale.Prefix, "")));

        return true;
    }
}
