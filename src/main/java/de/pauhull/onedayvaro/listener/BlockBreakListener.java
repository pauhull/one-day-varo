package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class BlockBreakListener implements Listener {

    private OneDayVaro oneDayVaro;

    public BlockBreakListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (!oneDayVaro.getIngamePhase().isCanBuild()) {
            event.setCancelled(true);
        }
    }

}
