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
    private DisplayScore coins, rank;

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
        new DisplayScore("§8§l× §f§lYour Coins");
        new DisplayScore();

        Group group = oneDayVaro.getGroupManager().getGroup(player);
        if (group != null) {
            rank = new DisplayScore("§8➥ " + group.getScoreboardName());
        } else {
            rank = new DisplayScore("§8➥ §6Unknown");
        }

        new DisplayScore("§8§l× §f§lRank");
        new DisplayScore();

        super.show();
    }

    @Override
    public void update() {
        String newCoins = "§8➥ §6" + oneDayVaro.getCoinsApi().getCoins(player.getUniqueId().toString());
        if (!newCoins.equals(coins.getScore().getEntry())) {
            coins.setName(newCoins);
        }

        Group group = oneDayVaro.getGroupManager().getGroup(player);
        String newRank;
        if (group != null) {
            newRank = "§8➥ " + group.getScoreboardName();
        } else {
            newRank = "§8➥ §6Unknown";
        }
        if (!rank.getScore().getEntry().equals(newRank)) {
            rank.setName(newRank);
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
