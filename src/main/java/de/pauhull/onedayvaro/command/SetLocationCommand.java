package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class SetLocationCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public SetLocationCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("setlocation").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.SetLocation)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(Locale.Prefix + "§c/setlocation <Location>");
            return true;
        }

        this.oneDayVaro.getLocationManager().setLocation(args[0], player.getLocation());
        player.sendMessage(Locale.LocationSet.replace("%LOCATION%", args[0]));

        return true;
    }
}
