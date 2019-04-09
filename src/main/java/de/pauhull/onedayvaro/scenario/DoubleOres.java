package de.pauhull.onedayvaro.scenario;

import de.pauhull.onedayvaro.OneDayVaro;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DoubleOres extends Scenario implements Listener {

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

    public DoubleOres(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.lore = Collections.singletonList("§7Alle Erze droppen doppelt");
        this.material = Material.DIAMOND_PICKAXE;
        this.displayName = "§9DoubleOres";
        this.durability = 0;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
        allScenarios.add(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!block.getType().name().contains("ORE")) {
            return;
        }

        Collection<ItemStack> drops;

        if (block.getType() == Material.IRON_ORE || block.getType() == Material.GOLD_ORE) {
            drops = new ArrayList<>();
            drops.add(new ItemStack(block.getType() == Material.IRON_ORE ? Material.IRON_INGOT : Material.GOLD_INGOT));
        } else {
            drops = block.getDrops(player.getItemInHand());
        }

        event.setCancelled(true);
        block.setType(Material.AIR);

        Location location = block.getLocation().add(0.5, 0, 0.5);

        Bukkit.getScheduler().scheduleSyncDelayedTask(oneDayVaro, () -> {
            for (ItemStack drop : drops) {
                for (int i = 0; i < 2; i++) {
                    location.getWorld().dropItemNaturally(location, drop);
                }
            }
        }, 0);
    }
}
