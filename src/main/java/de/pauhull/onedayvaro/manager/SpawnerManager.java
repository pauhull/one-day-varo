package de.pauhull.onedayvaro.manager;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul
 * on 04.04.2019
 *
 * @author pauhull
 */
public class SpawnerManager {

    private File file;
    private FileConfiguration config;

    public SpawnerManager(OneDayVaro oneDayVaro) {
        this.file = new File(oneDayVaro.getDataFolder(), "spawns.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public ArrayList<Location> getSpawns() {

        return new ArrayList(config.getList("Spawns", new ArrayList<Location>()));
    }

    public void setSpawns(List<Location> locations) {

        config.set("Spawns", locations);
    }

    public Location getLastSpawn() {

        List<Location> locations = getSpawns();
        return locations.get(locations.size() - 1);
    }

    public Location removeLastSpawn() {

        List<Location> locations = getSpawns();
        Location location = locations.remove(locations.size() - 1);
        setSpawns(locations);
        return location;
    }

    public void addSpawn(Location location) {

        List<Location> locations = getSpawns();
        locations.add(location);
        setSpawns(locations);
    }

}
