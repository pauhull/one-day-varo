package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageByArrowListener implements Listener {

    private OneDayVaro oneDayVaro;

    public PlayerDamageByArrowListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onPlayerDamageByArrow(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Arrow)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();

        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) arrow.getShooter();

        Team damagedTeam = Team.getTeam(player);
        Team shooterTeam = Team.getTeam(shooter);

        if ((damagedTeam != null && damagedTeam == shooterTeam) || oneDayVaro.getIngamePhase().isGracePeriod()) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

}
