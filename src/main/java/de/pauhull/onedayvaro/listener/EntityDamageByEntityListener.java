package de.pauhull.onedayvaro.listener;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.team.Team;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Paul
 * on 05.04.2019
 *
 * @author pauhull
 */
public class EntityDamageByEntityListener implements Listener {

    private OneDayVaro oneDayVaro;

    public EntityDamageByEntityListener(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        Bukkit.getPluginManager().registerEvents(this, oneDayVaro);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (oneDayVaro.isIngame()) {
            return;
        }

        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!oneDayVaro.getItemManager().getInviteToTeam().equals(damager.getItemInHand())) {
            return;
        }

        event.setCancelled(true);
        event.setDamage(0);

        if (oneDayVaro.getOptions().getTeamSize() <= 1) {

            damager.sendMessage(Locale.OnlyFFA);
            return;
        }

        Team damagerTeam = Team.getTeam(damager);
        Team damagedTeam = Team.getTeam(damaged);

        if (damagedTeam != null && damagedTeam.getInvited().contains(damager)) {

            damagedTeam.getInvited().remove(damager);
            damagedTeam.getMembers().add(damager);
            damagedTeam.broadcast(Locale.JoinedTeam.replace("%PLAYER%", damager.getName()));

            return;
        }

        if (damagedTeam == null) {

            if (damagerTeam == null) {
                damagerTeam = Team.create(damager);
                damager.sendMessage(Locale.TeamCreated);
            }

            if (damager != damagerTeam.getOwner()) {
                damager.sendMessage(Locale.NoInvites);
                return;
            }

            if (damagerTeam.getInvited().contains(damaged)) {

                damagerTeam.getInvited().remove(damaged);
                damaged.sendMessage(Locale.UninviteReceived.replace("%PLAYER%", damager.getName()));
                damager.sendMessage(Locale.UninviteSent.replace("%PLAYER%", damaged.getName()));

            } else {

                damagerTeam.getInvited().add(damaged);
                damaged.sendMessage(Locale.InviteReceived.replace("%PLAYER%", damager.getName()));
                damager.sendMessage(Locale.InviteSent.replace("%PLAYER%", damaged.getName()));

            }
        } else {

            if (damagedTeam == damagerTeam) {

                if (damager == damagerTeam.getOwner()) {

                    damagerTeam.getMembers().remove(damaged);
                    damager.sendMessage(Locale.RemovedFromTeam.replace("%PLAYER%", damaged.getName()));
                    damagerTeam.broadcast(Locale.LeftTeam.replace("%PLAYER%", damaged.getName()));

                } else {

                    damager.sendMessage(Locale.AlreadyInTeam);

                }

            } else {

                damager.sendMessage(Locale.NotInYourTeam);

            }

        }
    }
}
