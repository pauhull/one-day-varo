package de.pauhull.onedayvaro;

import de.pauhull.onedayvaro.command.SetLocationCommand;
import de.pauhull.onedayvaro.display.VaroScoreboard;
import de.pauhull.onedayvaro.group.GroupManager;
import de.pauhull.onedayvaro.listener.*;
import de.pauhull.onedayvaro.manager.LocationManager;
import de.pauhull.onedayvaro.util.CoinApiHook;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Permissions;
import de.pauhull.scoreboard.CustomScoreboard;
import de.pauhull.scoreboard.ScoreboardManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class OneDayVaro extends JavaPlugin {

    @Getter
    private static OneDayVaro instance;

    @Getter
    private ScoreboardManager scoreboardManager;

    @Getter
    private CoinApiHook coinApiHook;

    @Getter
    private GroupManager groupManager;

    @Getter
    @Setter
    private boolean ingame;

    @Getter
    private LocationManager locationManager;

    @Getter
    private FileConfiguration config;

    @Getter
    private boolean pluginEnabled;

    @Override
    public void onEnable() {
        instance = this;

        this.scoreboardManager = new ScoreboardManager(this, VaroScoreboard.class);
        this.groupManager = new GroupManager(this);
        this.coinApiHook = new CoinApiHook();
        this.locationManager = new LocationManager(this);
        this.config = copyAndLoad("config.yml", new File(getDataFolder(), "config.yml"));
        this.pluginEnabled = config.getBoolean("Enabled");

        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.updateTeam(player);
            CustomScoreboard scoreboard = new VaroScoreboard(player);
            scoreboard.show();
            scoreboardManager.getScoreboards().put(player, scoreboard);
        }

        if (pluginEnabled) {
            new AsyncPlayerChatListener(this);
            new BlockBreakListener(this);
            new BlockPlaceListener(this);
            new EntityDamageListener(this);
            new FoodLevelChangeListener(this);
            new InventoryClickListener(this);
            new PlayerDropItemListener(this);
            new PlayerInteractListener(this);
            new PlayerJoinListener(this);
            new PlayerPickupItemListener(this);
            new PlayerQuitListener(this);
        }

        new SetLocationCommand(this);

        Locale.load();
        Permissions.load();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public FileConfiguration copyAndLoad(String resource, File copyTo) {
        if (!copyTo.exists()) {
            copyTo.getParentFile().mkdirs();

            try {
                Files.copy(getResource(resource), copyTo.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(new InputStreamReader(new FileInputStream(copyTo), Charsets.UTF_8));
            return config;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }

}
