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
 * on 06.04.2019
 *
 * @author pauhull
 */
public class GlobalMuteCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public GlobalMuteCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("globalmute").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.Globalmute)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        oneDayVaro.setGlobalMute(!oneDayVaro.isGlobalMute());
        Bukkit.broadcastMessage(oneDayVaro.isGlobalMute() ? Locale.GlobalmuteActivated : Locale.GlobalmuteDeactivated);

        return true;
    }
}
