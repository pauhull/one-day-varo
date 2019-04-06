package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Paul
 * on 04.04.2019
 *
 * @author pauhull
 */
public class RemoveSpawnCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public RemoveSpawnCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("removespawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.AddSpawn)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();

        String id = Integer.toString(oneDayVaro.getSpawnerManager().getSpawns(world).size());

        if (id.equals("0")) {
            sender.sendMessage(Locale.NoSpawns);
            return true;
        }

        oneDayVaro.getSpawnerManager().removeLastSpawn(world);
        sender.sendMessage(Locale.SpawnRemoved.replace("%ID%", id).replace("%WORLD%", world.getName()));

        return true;
    }

}
