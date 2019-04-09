package de.pauhull.onedayvaro.scenario;

import de.pauhull.onedayvaro.OneDayVaro;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeBomb extends Scenario implements Listener {

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

    public TimeBomb(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.lore = Arrays.asList("§7Alle Items sind in einer Kiste,", "§7die nach einer Weile explodiert.");
        this.material = Material.CHEST;
        this.displayName = "§5TimeBomb";
        this.durability = 0;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
        allScenarios.add(this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        event.getDrops().clear();
        Location location = event.getEntity().getLocation();
        location.getBlock().setType(Material.CHEST);
        location.getBlock().getRelative(BlockFace.NORTH).setType(Material.CHEST);
        ((Chest) location.getBlock().getState()).getBlockInventory().addItem(drops.toArray(new ItemStack[0]));

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -1, -1), EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setCustomName("");
        armorStand.setCustomNameVisible(true);

        AtomicInteger countdown = new AtomicInteger(30);
        AtomicInteger task = new AtomicInteger();
        task.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(oneDayVaro, () -> {

            int i = countdown.getAndDecrement();

            if (i > 0) {
                armorStand.setCustomName("§7Kiste explodiert in §e" + i + "§7 Sekunden");
            } else {
                location.getBlock().setType(Material.AIR);
                location.getBlock().getRelative(BlockFace.NORTH).setType(Material.AIR);
                armorStand.remove();
                location.getWorld().createExplosion(location, 10f);
                Bukkit.getScheduler().cancelTask(task.get());
            }

        }, 0, 20));
    }
}
