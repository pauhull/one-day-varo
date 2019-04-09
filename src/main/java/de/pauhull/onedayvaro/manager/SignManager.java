package de.pauhull.onedayvaro.manager;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignManager {

    private OneDayVaro oneDayVaro;
    private FileConfiguration config;
    private File file;

    public SignManager(OneDayVaro oneDayVaro) {

        this.oneDayVaro = oneDayVaro;
        this.file = new File(oneDayVaro.getDataFolder(), "signs.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setTopWall() {

        Map<Integer, List<Location>> signs = getSigns();

        for (int top : signs.keySet()) {

            oneDayVaro.getStatsTable().getTop(top, (stats, name) -> {
                for (Location location : signs.get(top)) {

                    Block block = location.getBlock();

                    if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
                        delete(location);
                        continue;
                    }

                    Sign sign = (Sign) block.getState();

                    for (int i = 0; i < 4; i++) {

                        String line = null;

                        if (oneDayVaro.getConfig().isString("TopSignFormat." + (i + 1))) {

                            line = ChatColor.translateAlternateColorCodes('&', oneDayVaro.getConfig().getString("TopSignFormat." + (i + 1)));

                            if (stats != null && name != null) {

                                line = line.replace("%PLAYER%", name)
                                        .replace("%RANK%", Integer.toString(top))
                                        .replace("%KILLS%", Integer.toString(stats.getKills()))
                                        .replace("%DEATHS%", Integer.toString(stats.getDeaths()))
                                        .replace("%WINS%", Integer.toString(stats.getWins()))
                                        .replace("%PLAYED_GAMES%", Integer.toString(stats.getPlayedGames()))
                                        .replace("%KD%", stats.getDeaths() == 0 ? "NaN" : String.format("%.2f", stats.getKd()));
                            } else {

                                line = line.replace("%PLAYER%", "?")
                                        .replace("%RANK%", Integer.toString(top))
                                        .replace("%KILLS%", "?")
                                        .replace("%DEATHS%", "?")
                                        .replace("%WINS%", "?")
                                        .replace("%PLAYED_GAMES%", "?")
                                        .replace("%KD%", "?");
                            }
                        }

                        sign.setLine(i, line);
                    }

                    sign.update();

                    Block skullBlock = block.getRelative(BlockFace.UP);
                    if (skullBlock.getType() != Material.SKULL) {
                        continue;
                    }

                    Skull skull = (Skull) skullBlock.getState();
                    skull.setSkullType(SkullType.PLAYER);
                    skull.setOwner(name == null ? "MHF_Question" : name);
                    skull.update();
                }
            });
        }
    }

    public void save() {

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSign(Location location, int top) {

        config.set(locationToString(location), top);
        save();
    }

    public boolean delete(Location location) {

        String locationString = locationToString(location);

        if (!config.isSet(locationString)) {
            return false;
        }

        config.set(locationString, null);
        save();

        return true;
    }

    public Map<Integer, List<Location>> getSigns() {

        Map<Integer, List<Location>> signs = new HashMap<>();

        for (String locationString : config.getKeys(false)) {

            int top = config.getInt(locationString);

            if (!signs.containsKey(top)) {
                signs.put(top, new ArrayList<>());
            }

            Location location = stringToLocation(locationString);
            signs.get(top).add(location);
        }

        return signs;
    }

    public boolean isSign(Location location) {

        return config.isSet(locationToString(location));
    }

    private String locationToString(Location location) {

        return location.getWorld().getName() + "/" + location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ();
    }

    private Location stringToLocation(String string) {

        String[] arr = string.split("/");
        World world = Bukkit.getWorld(arr[0]);
        double x = Double.valueOf(arr[1]);
        double y = Double.valueOf(arr[2]);
        double z = Double.valueOf(arr[3]);

        return new Location(world, x, y, z);
    }

}
