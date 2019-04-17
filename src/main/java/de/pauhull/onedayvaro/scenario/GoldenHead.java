package de.pauhull.onedayvaro.scenario;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.List;

public class GoldenHead extends Scenario implements Listener {

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

    public GoldenHead(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.lore = Collections.singletonList("§7A skull spawns, when a player dies");
        this.material = Material.SKULL_ITEM;
        this.displayName = "§6GoldenHead";
        this.durability = 3;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
        allScenarios.add(this);

        ShapedRecipe recipe = new ShapedRecipe(new ItemBuilder().material(Material.GOLDEN_APPLE).displayName("§dGoldenHead").build())
                .shape("GGG", "GHG", "GGG").setIngredient('G', Material.GOLD_INGOT).setIngredient('H', Material.SKULL_ITEM);
        Bukkit.getServer().addRecipe(recipe);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        TimeBomb timeBomb = Scenario.getInstance(TimeBomb.class);
        if (timeBomb != null && timeBomb.isEnabled()) {

            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) stack.getItemMeta();
            meta.setOwner(player.getName());
            stack.setItemMeta(meta);
            event.getDrops().add(stack);
        } else {

            Block block = player.getLocation().getBlock();
            block.setType(Material.FENCE);
            Block skull = block.getRelative(BlockFace.UP);
            skull.setType(Material.SKULL);
            ((Skull) skull.getState()).setSkullType(SkullType.PLAYER);
            ((Skull) skull.getState()).setOwner(player.getName());
            skull.getState().update();
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {

        if (!enabled || !oneDayVaro.isIngame()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack stack = event.getItem();

        if (stack.getType() == Material.GOLDEN_APPLE && stack.hasItemMeta() && "§dGoldenHead".equals(stack.getItemMeta().getDisplayName())) {
            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 2));
        }
    }

}
