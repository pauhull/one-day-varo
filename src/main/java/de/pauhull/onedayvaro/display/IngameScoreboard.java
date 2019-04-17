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
public class IngameScoreboard extends CustomScoreboard {

    private OneDayVaro oneDayVaro;
    private DisplayScore coins, kills;

    public IngameScoreboard(Player player) {
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
        this.kills = new DisplayScore("§8➥ §60");
        new DisplayScore("§8§l× §f§lKills");
        new DisplayScore();

        super.show();
    }

    @Override
    public void update() {
        String newCoins = "§8➥ §6" + oneDayVaro.getCoinsApi().getCoins(player.getUniqueId().toString());
        if (!newCoins.equals(coins.getScore().getEntry())) {
            coins.setName(newCoins);
        }

        int kills = 0;
        if (oneDayVaro.getIngamePhase().getKills().containsKey(player.getName())) {
            kills = oneDayVaro.getIngamePhase().getKills().get(player.getName());
        }
        String newKills = "§8➥ §6" + kills;
        if (!newKills.equals(this.kills.getScore().getEntry())) {
            this.kills.setName(newKills);
        }
    }

    @Override
    public void updateTeam(Player player) {
        Group group = oneDayVaro.getGroupManager().getGroup(player);

        if (group != null) {

            de.pauhull.onedayvaro.team.Team odvTeam = de.pauhull.onedayvaro.team.Team.getTeam(player);
            String name = "";

            if (odvTeam != null) {
                name = odvTeam.getName();
                if (name.length() > 4) {
                    name = name.substring(0, 4);
                }
            }

            String teamName = String.format("%02d", group.getId()) + name + player.getName();
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

            int kills = 0;
            if (oneDayVaro.getIngamePhase().getKills().containsKey(player.getName())) {
                kills = oneDayVaro.getIngamePhase().getKills().get(player.getName());
            }
            String suffix = " §8[§e" + kills + "§8]";

            team.setSuffix(suffix);
            team.setPrefix(prefix);
            team.addEntry(player.getName());
        }
    }

}
