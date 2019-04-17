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
 * on 02.04.2019
 *
 * @author pauhull
 */
public class ConfigCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public ConfigCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("config").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.Options) && !sender.getName().equalsIgnoreCase(oneDayVaro.getHost())) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        oneDayVaro.getOptionsInventory().show(player);

        return true;
    }
}
