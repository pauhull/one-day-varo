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
public class AddSpawnCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public AddSpawnCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("addspawn").setExecutor(this);
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
        oneDayVaro.getSpawnerManager().addSpawn(world, player.getLocation());
        String id = Integer.toString(oneDayVaro.getSpawnerManager().getSpawns(world).size());
        player.sendMessage(Locale.SpawnSet.replace("%ID%", id).replace("%WORLD%", world.getName()));

        return true;
    }

}
