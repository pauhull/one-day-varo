package de.pauhull.onedayvaro.group;

import de.pauhull.onedayvaro.OneDayVaro;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class GroupManager {

    @Getter
    private List<Group> groups;

    public GroupManager(OneDayVaro oneDayVaro) {

        loadGroupsFromConfig(oneDayVaro.copyAndLoad("groups.yml", new File(oneDayVaro.getDataFolder(), "groups.yml")));
    }

    private void loadGroupsFromConfig(FileConfiguration config) {

        this.groups = new ArrayList<>();

        int id = 0;
        for (String groupName : config.getConfigurationSection("Groups").getKeys(false)) {
            String chatFormat = ChatColor.translateAlternateColorCodes('&', config.getString("Groups." + groupName + ".Chat"));
            String tablistPrefix = ChatColor.translateAlternateColorCodes('&', config.getString("Groups." + groupName + ".TablistPrefix"));
            String scoreboardName = ChatColor.translateAlternateColorCodes('&', config.getString("Groups." + groupName + ".ScoreboardName"));
            String permission = config.getString("Groups." + groupName + ".Permission");

            Group group = Group.builder()
                    .id(id++)
                    .chatFormat(chatFormat)
                    .tablistPrefix(tablistPrefix)
                    .scoreboardName(scoreboardName)
                    .permission(permission)
                    .build();

            groups.add(group);
        }
    }

    public Group getGroup(Player player) {

        for (Group group : groups) {
            if (group.getPermission() == null) {

                return group;
            } else {

                if (player.hasPermission(group.getPermission())) {
                    return group;
                }
            }
        }

        return null;
    }

}
