package de.pauhull.onedayvaro.scenario;

import de.pauhull.onedayvaro.OneDayVaro;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class CutClean extends Scenario implements Listener {

    @Getter
    private String displayName;

    @Getter
    private Material material;

    @Getter
    private short durability;

    @Getter
    @Setter
    private boolean enabled;

    @Getter
    private List<String> lore;

    private OneDayVaro oneDayVaro;

    public CutClean(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.lore = Collections.singletonList("§7Alle Erze droppen geschmolzen");
        this.material = Material.IRON_ORE;
        this.displayName = "§cCutClean";
        this.durability = 0;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
        allScenarios.add(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        DoubleOres doubleOres = Scenario.getInstance(DoubleOres.class);

        if (doubleOres != null && doubleOres.isEnabled()) {
            return;
        }

        Block block = event.getBlock();

        if (block.getType() == Material.IRON_ORE || block.getType() == Material.GOLD_ORE) {

            ItemStack drop = new ItemStack(block.getType() == Material.IRON_ORE ? Material.IRON_INGOT : Material.GOLD_INGOT);
            Location location = block.getLocation().add(0.5, 0, 0.5);
            Bukkit.getScheduler().scheduleSyncDelayedTask(oneDayVaro, () -> location.getWorld().dropItemNaturally(location, drop), 0);

            event.setCancelled(true);
            block.setType(Material.AIR);
        }
    }
}
