package de.pauhull.onedayvaro.display;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.group.Group;
import de.pauhull.scoreboard.CustomScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class LobbyScoreboard extends CustomScoreboard {

    private OneDayVaro oneDayVaro;
    private DisplayScore coins;

    public LobbyScoreboard(Player player) {
        super(player, "varoscoreboard", "§6§lElite§f§lEmpire");
        this.descending = false;
        this.nextScoreID = 1;
        this.oneDayVaro = OneDayVaro.getInstance();
    }

    @Override
    public void show() {
        new DisplayScore();
        new DisplayScore("§8➥ §6ts.EliteEmpire.net");
        new DisplayScore("§8§l× §f§lTeamspeak");
        new DisplayScore();
        new DisplayScore("§8➥ §6Orbital-Hub.com");
        new DisplayScore("§8§l× §f§lWebsite");
        new DisplayScore();
        coins = new DisplayScore("§8➥ §6" + oneDayVaro.getCoinsApi().getCoins(player.getUniqueId().toString()));
        new DisplayScore("§8§l× §f§lDeine Coins");
        new DisplayScore();

        Group group = oneDayVaro.getGroupManager().getGroup(player);
        if (group != null) {
            new DisplayScore("§8➥ " + group.getScoreboardName());
        } else {
            new DisplayScore("§8➥ §6Unbekannt");
        }

        new DisplayScore("§8§l× §f§lRang");
        new DisplayScore();

        super.show();
    }

    @Override
    public void update() {
        String newCoins = "§8➥ §6" + oneDayVaro.getCoinsApi().getCoins(player.getUniqueId().toString());
        if (!newCoins.equals(coins.getScore().getEntry())) {
            coins.setName(newCoins);
        }
    }

    @Override
    public void updateTeam(Player player) {
        Group group = oneDayVaro.getGroupManager().getGroup(player);

        if (group != null) {
            String teamName = String.format("%02d", group.getId()) + player.getName();
            if (teamName.length() > 16) {
                teamName = teamName.substring(0, 16);
            }

            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
            }

            String prefix = group.getTablistPrefix();
            if (prefix.length() > 16) {
                prefix = prefix.substring(0, 16);
            }
            team.setPrefix(prefix);
            team.addEntry(player.getName());
        }
    }

}
