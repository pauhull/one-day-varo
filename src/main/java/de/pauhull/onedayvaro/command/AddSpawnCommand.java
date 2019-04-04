package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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


        return true;
    }

}
