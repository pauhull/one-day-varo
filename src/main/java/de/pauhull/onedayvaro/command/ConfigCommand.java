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

        if (!sender.hasPermission(Permissions.Options)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (oneDayVaro.isIngame()) {
            player.sendMessage(Locale.AlreadyIngame);
            return true;
        }

        oneDayVaro.getOptionsInventory().show(player);

        return true;
    }
}
