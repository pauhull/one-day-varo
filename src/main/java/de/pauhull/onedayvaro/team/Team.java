package de.pauhull.onedayvaro.team;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul
 * on 05.04.2019
 *
 * @author pauhull
 */
public class Team {

    @Getter
    private static List<Team> allTeams = new ArrayList<>();

    @Getter
    private Player owner;

    @Getter
    private List<Player> members;

    @Getter
    private List<Player> invited;

    @Getter
    @Setter
    private String name;

    private Team(Player owner, List<Player> members, List<Player> invited, String name) {

        this.owner = owner;
        this.members = members;
        this.invited = invited;
        this.name = name;
    }

    public static Team create(Player owner) {

        List<Player> members = new ArrayList<>();
        members.add(owner);
        Team team = new Team(owner, members, new ArrayList<>(), "#EliteEmpire");
        allTeams.add(team);

        return team;
    }

    public static Team getTeam(Player player) {

        if (player == null) return null;

        for (Team team : allTeams) {
            if (team.getMembers().contains(player)) {
                return team;
            }
        }

        return null;
    }

    public void delete() {

        members.clear();
        invited.clear();
        owner = null;
        allTeams.remove(this);
    }

    public void broadcast(String message) {

        for (Player player : members) {
            player.sendMessage(message);
        }
    }

}
