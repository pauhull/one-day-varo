package de.pauhull.onedayvaro.manager;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<World> getWorlds() {

        List<World> worlds = new ArrayList<>();
        config.getConfigurationSection("Spawns").getKeys(false).forEach(world -> worlds.add(Bukkit.getWorld(world)));
        return worlds;
    }

    public ArrayList<Location> getSpawns(World world) {

        if (!config.isList("Spawns." + world.getName())) {
            return new ArrayList<>();
        }

        return new ArrayList(config.getList("Spawns." + world.getName(), new ArrayList<Location>()));
    }

    public void setSpawns(World world, List<Location> locations) {

        config.set("Spawns." + world.getName(), locations);
        save();
    }

    public Location getLastSpawn(World world) {

        List<Location> locations = getSpawns(world);
        return locations.get(locations.size() - 1);
    }

    public Location removeLastSpawn(World world) {

        List<Location> locations = getSpawns(world);
        Location location = locations.remove(locations.size() - 1);
        setSpawns(world, locations);
        return location;
    }

    public void addSpawn(World world, Location location) {

        List<Location> locations = getSpawns(world);
        locations.add(location);
        setSpawns(world, locations);
    }

}
