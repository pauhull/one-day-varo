package de.pauhull.onedayvaro;

import cloud.evaped.coinsapi.API.CoinsAPI;
import de.pauhull.onedayvaro.command.*;
import de.pauhull.onedayvaro.data.MySQL;
import de.pauhull.onedayvaro.data.table.StatsTable;
import de.pauhull.onedayvaro.display.LobbyScoreboard;
import de.pauhull.onedayvaro.group.GroupManager;
import de.pauhull.onedayvaro.inventory.OptionsInventory;
import de.pauhull.onedayvaro.inventory.TeamInventory;
import de.pauhull.onedayvaro.listener.*;
import de.pauhull.onedayvaro.manager.ItemManager;
import de.pauhull.onedayvaro.manager.LocationManager;
import de.pauhull.onedayvaro.manager.SignManager;
import de.pauhull.onedayvaro.manager.SpawnerManager;
import de.pauhull.onedayvaro.phase.IngamePhase;
import de.pauhull.onedayvaro.util.Locale;
import de.pauhull.onedayvaro.util.Options;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Getter
    private OptionsInventory optionsInventory;

    @Getter
    private Options options;

    @Getter
    private ItemManager itemManager;

    @Getter
    private SpawnerManager spawnerManager;

    @Getter
    private IngamePhase ingamePhase;

    @Getter
    @Setter
    private boolean globalMute;

    @Getter
    private TeamInventory teamInventory;

    @Getter
    private CoinsAPI coinsApi;

    @Getter
    private FileConfiguration mysqlConfig;

    @Getter
    private ExecutorService executorService;

    @Getter
    private MySQL mySQL;

    @Getter
    private StatsTable statsTable;

    @Getter
    private SignManager signManager;

    @Override
    public void onEnable() {
        instance = this;

        this.options = new Options();
        this.itemManager = new ItemManager();
        this.scoreboardManager = new ScoreboardManager(this, LobbyScoreboard.class);
        this.groupManager = new GroupManager(this);
        this.coinsApi = new CoinsAPI();
        this.spawnerManager = new SpawnerManager(this);
        this.locationManager = new LocationManager(this);
        this.config = copyAndLoad("config.yml", new File(getDataFolder(), "config.yml"));
        this.mysqlConfig = copyAndLoad("mysql.yml", new File(getDataFolder(), "mysql.yml"));
        this.pluginEnabled = config.getBoolean("Enabled");
        this.optionsInventory = new OptionsInventory(this);
        this.ingamePhase = new IngamePhase(this);
        this.teamInventory = new TeamInventory(this);
        this.signManager = new SignManager(this);

        this.mySQL = new MySQL(mysqlConfig.getString("MySQL.Host"),
                mysqlConfig.getString("MySQL.Port"),
                mysqlConfig.getString("MySQL.Database"),
                mysqlConfig.getString("MySQL.User"),
                mysqlConfig.getString("MySQL.Password"),
                mysqlConfig.getBoolean("MySQL.SSL"));

        if (!this.mySQL.connect()) {
            Bukkit.getConsoleSender().sendMessage("§c[ODVPlugin] Konnte nicht zur MySQL-Datenbank verbinden. Plugin wird gestoppt...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.executorService = Executors.newFixedThreadPool(10);
        this.statsTable = new StatsTable(mySQL, executorService);

        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.updateTeam(player);
            CustomScoreboard scoreboard = new LobbyScoreboard(player);
            scoreboard.show();
            scoreboardManager.getScoreboards().put(player, scoreboard);
        }

        if (pluginEnabled) {

            new AsyncPlayerChatListener(this);
            new BlockBreakListener(this);
            new BlockPlaceListener(this);
            new EntityDamageByEntityListener(this);
            new EntityDamageListener(this);
            new EntityExplodeListener(this);
            new FoodLevelChangeListener(this);
            new InventoryClickListener(this);
            new PlayerAchievementAwardedListener(this);
            new PlayerDropItemListener(this);
            new PlayerInteractListener(this);
            new PlayerJoinListener(this);
            new PlayerLoginListener(this);
            new PlayerPickupItemListener(this);
            new PlayerQuitListener(this);
            new PlayerPortalListener(this);
            new PlayerDeathListener(this);
            new PlayerDamageByArrowListener(this);

            new ConfigCommand(this);
            new StartCommand(this);
            new TeamsCommand(this);
            new GlobalMuteCommand(this);
        }

        new SignChangeListener(this);
        new AddSpawnCommand(this);
        new RemoveSpawnCommand(this);
        new SetLocationCommand(this);

        Locale.load();
        Permissions.load();

        signManager.setTopWall();
    }

    @Override
    public void onDisable() {
        instance = null;

        for (CustomScoreboard scoreboard : scoreboardManager.getScoreboards().values()) {
            scoreboard.delete();
        }

        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
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
