package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    private OneDayVaro oneDayVaro;

    public SignChangeListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {

        Player player = event.getPlayer();

        if (!event.getLine(0).equalsIgnoreCase("[Top]") || !player.hasPermission(Permissions.CreateSign)) {
            return;
        }

        int top;
        try {
            top = Integer.parseInt(event.getLine(1));
        } catch (IllegalArgumentException e) {
            event.getBlock().breakNaturally();
            player.sendMessage(Locale.InvalidNumber);
            return;
        }

        if (top < 1) {
            event.getBlock().breakNaturally();
            player.sendMessage(Locale.InvalidNumber);
        }

        oneDayVaro.getSignManager().saveSign(event.getBlock().getLocation(), top);
        player.sendMessage(Locale.SignPlaced);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
            return;
        }

        Sign sign = (Sign) block.getState();
        Location location = sign.getLocation();

        if (!oneDayVaro.getSignManager().isSign(location)) {
            return;
        }

        if (!player.hasPermission(Permissions.CreateSign)) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(false);
        oneDayVaro.getSignManager().delete(location);
        player.sendMessage(Locale.SignDestroyed);
    }

}
