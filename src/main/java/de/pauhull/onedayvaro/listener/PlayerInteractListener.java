package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.Locale;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class PlayerInteractListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerInteractListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack stack = event.getItem();

        if (!this.oneDayVaro.getIngamePhase().isCanBuild()) {
            event.setCancelled(true);

            if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
                return;
            }

            if (oneDayVaro.getItemManager().getBackToLobby().equals(stack)) {

                player.kickPlayer("");

            } else if (oneDayVaro.getItemManager().getInviteToTeam().equals(stack)) {

                if (oneDayVaro.getOptions().getTeamSize() <= 1) {
                    player.sendMessage(Locale.OnlyFFA);
                    return;
                }

                oneDayVaro.getTeamInventory().show(player);
            } else if (oneDayVaro.getItemManager().getRename().equals(stack)) {

                Team team = Team.getTeam(player);

                if (team == null) {
                    return;
                }

                new AnvilGUI(oneDayVaro, player, team.getName(), (anvilPlayer, reply) -> {

                    if (reply.length() > 16) {
                        anvilPlayer.sendMessage(Locale.TeamNameTooLong);
                        return reply;
                    }

                    String name = ChatColor.translateAlternateColorCodes('&', reply);
                    team.setName(name);
                    anvilPlayer.sendMessage(Locale.TeamRenamed.replace("%NAME%", name));
                    return null;
                });
            }
        }
    }
}
