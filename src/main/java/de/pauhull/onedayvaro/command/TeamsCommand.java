package de.pauhull.onedayvaro.command;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Paul
 * on 06.04.2019
 *
 * @author pauhull
 */
public class TeamsCommand implements CommandExecutor {

    private OneDayVaro oneDayVaro;

    public TeamsCommand(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        oneDayVaro.getCommand("teams").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(Permissions.Teams)) {
            sender.sendMessage(Locale.NoPermission);
            return true;
        }

        if (Team.getAllTeams().isEmpty()) {
            sender.sendMessage(Locale.OnlyFFA);
            return true;
        }

        sender.sendMessage(" ");
        sender.sendMessage(Locale.Prefix + "Alle Teams:");
        for (Team team : Team.getAllTeams()) {
            AtomicInteger index = new AtomicInteger();
            String[] names = new String[team.getMembers().size()];
            team.getMembers().forEach(player -> names[index.getAndIncrement()] = player.getName());
            sender.sendMessage("§6" + team.getName() + "§8: §f" +
                    Arrays.toString(names).replace("[", "").replace("]", ""));
        }
        sender.sendMessage(" ");

        return true;
    }
}
