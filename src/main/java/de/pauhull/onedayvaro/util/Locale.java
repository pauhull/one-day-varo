package de.pauhull.onedayvaro.util;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class Locale {

    public static String Prefix = "";
    public static String LobbyJoin = "";
    public static String LobbyLeave = "";
    public static String NoPermission = "";
    public static String LocationNotSet = "";
    public static String LocationSet = "";
    public static String AlreadyIngame = "";
    public static String OptionChanged = "";
    public static String TeamSizeChanged = "";
    public static String ProtectionPeriodChanged = "";
    public static String SpawnSet = "";
    public static String SpawnRemoved = "";
    public static String NoSpawns = "";
    public static String TeamCreated = "";
    public static String NoInvites = "";
    public static String InviteSent = "";
    public static String InviteReceived = "";
    public static String RemovedFromTeam = "";
    public static String JoinedTeam = "";
    public static String OnlyFFA = "";
    public static String LeftTeam = "";
    public static String AlreadyInTeam = "";
    public static String NotInYourTeam = "";
    public static String UninviteReceived = "";
    public static String UninviteSent = "";
    public static String GlobalmuteActivated = "";
    public static String GlobalmuteDeactivated = "";
    public static String NoChat = "";
    public static String NoTeam = "";
    public static String TeamDeleted = "";
    public static String TeamNameTooLong = "";
    public static String TeamRenamed = "";
    public static String WorldChanged = "";
    public static String NoSpawnFound = "";
    public static String NotAnySpawns = "";
    public static String GameStartsIn = "";
    public static String GameStarted = "";
    public static String GracePeriodEnd = "";
    public static String NetherEnabled = "";
    public static String NetherDisabled = "";
    public static String CoinsAdded = "";
    public static String BorderChanged = "";
    public static String NotEnoughPlayers = "";
    public static String ScenarioDisabled = "";
    public static String ScenarioEnabled = "";
    public static String GracePeriodEndsIn = "";
    public static String PlayerWonGame = "";
    public static String TeamWonGame = "";
    public static String TopKills = "";
    public static String SignPlaced = "";
    public static String SignDestroyed = "";
    public static String InvalidNumber = "";
    public static String ServerRestartsIn = "";
    public static String TeamFull = "";

    public static void load() {

        load(OneDayVaro.getInstance().copyAndLoad("messages.yml", new File(OneDayVaro.getInstance().getDataFolder(), "messages.yml")));
    }

    public static void load(FileConfiguration config) {

        for (Field field : Locale.class.getDeclaredFields()) {
            if (config.isString(field.getName())) {
                try {
                    field.set(null, Prefix + ChatColor.translateAlternateColorCodes('&', config.getString(field.getName())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("[ODVPlugin] Missing field in messages.yml: \"" + field.getName() + "\"");
            }
        }
    }

}
