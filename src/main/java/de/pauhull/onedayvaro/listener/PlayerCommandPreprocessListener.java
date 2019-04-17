package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {


    public PlayerCommandPreprocessListener(OneDayVaro oneDayVaro) {

        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        String command = event.getMessage().toLowerCase();
        Player player = event.getPlayer();

        if (isCommand(command, "/help") || isCommand(command, "/bukkit:help") || isCommand(command, "/minecraft:help")
                || isCommand(command, "/?") || isCommand(command, "/minecraft:?") || isCommand(command, "/bukkit:?")
                || isCommand(command, "/pl") || isCommand(command, "/bukkit:pl")
                || isCommand(command, "/plugins") || isCommand(command, "/bukkit:plugins")
                || isCommand(command, "/ver") || isCommand(command, "/bukkit:ver")
                || isCommand(command, "/version") || isCommand(command, "/bukkit:version")
                || isCommand(command, "/icanhasbukkit") || isCommand(command, "/bukkit:icanhasbukkit")) {

            for (String serverInfo : Locale.ServerInfo) {
                player.sendMessage(serverInfo);
            }

            event.setCancelled(true);
        }
    }

    private boolean isCommand(String command, String check) {

        if (command.equals(check)) {
            return true;
        }

        if (check.startsWith(command + " ")) {
            return true;
        }

        return false;
    }

}
