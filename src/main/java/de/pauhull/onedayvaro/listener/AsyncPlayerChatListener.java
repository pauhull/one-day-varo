package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.group.Group;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class AsyncPlayerChatListener implements Listener {

    private OneDayVaro oneDayVaro;

    public AsyncPlayerChatListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if (!player.hasPermission(Permissions.GlobalmuteBypass) && oneDayVaro.isGlobalMute()) {

            event.setCancelled(true);
            player.sendMessage(Locale.NoChat);
            return;
        }

        Group group = oneDayVaro.getGroupManager().getGroup(player);

        if (group == null) {
            return;
        }

        event.setFormat(group.getChatFormat().replace("%PLAYER%", player.getName())
                .replace("%MESSAGE%", event.getMessage())
                .replace("%", "%%"));
    }

}
