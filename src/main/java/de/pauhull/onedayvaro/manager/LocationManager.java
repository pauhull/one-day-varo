package de.pauhull.onedayvaro.manager;

import de.pauhull.onedayvaro.OneDayVaro;
import de.pauhull.onedayvaro.util.Locale;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by Paul
 * on 01.04.2019
 *
 * @author pauhull
 */
public class LocationManager {

    private File file;
    private FileConfiguration config;

    public LocationManager(OneDayVaro oneDayVaro) {

        this.file = new File(oneDayVaro.getDataFolder(), "locations.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLocation(String path, Location location) {

        config.set(path, location);
        this.save();
    }

    public Location getLocation(String path) {

        return (Location) config.get(path);
    }

    public void teleport(String path, Player... players) {

        Location location = getLocation(path);

        if (location == null) {

            for (Player player : players) {
                player.sendMessage(Locale.LocationNotSet.replace("%LOCATION%", path));
            }
            return;
        }

        for (Player player : players) {
            player.teleport(location);
        }
    }

}
