package de.pauhull.onedayvaro.util;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class Locale {

    public static String HostSet = "";

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
    public static String TeamFull = "";
    public static String HelpOp = "";
    public static String HelpHost = "";
    public static String CoinsRemoved = "";
    public static List<String> ServerInfo = new ArrayList<>();
    public static List<String> Stats = new ArrayList<>();

    private static FileConfiguration config;

    public static void load() {

        load(OneDayVaro.getInstance().copyAndLoad("messages.yml", new File(OneDayVaro.getInstance().getDataFolder(), "messages.yml")));
    }

    public static void load(FileConfiguration config) {

        Locale.config = config;

        for (Field field : Locale.class.getDeclaredFields()) {

            if (Modifier.isPrivate(field.getModifiers())) {
                continue;
            }

            if (config.isString(field.getName()) && field.getType().isAssignableFrom(String.class)) {
                try {
                    StringBuilder builder = new StringBuilder();

                    if (!field.getName().equals("Prefix")) {
                        builder.append(Prefix);
                    }

                    builder.append(config.getString(field.getName()));

                    field.set(null, ChatColor.translateAlternateColorCodes('&', builder.toString()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (config.isList(field.getName()) && field.getType().isAssignableFrom(List.class)) {
                try {

                    List<String> list = new ArrayList<>();
                    for (String s : config.getStringList(field.getName())) {
                        list.add(ChatColor.translateAlternateColorCodes('&', s));
                    }

                    field.set(null, list);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("[ODVPlugin] Missing or wrong type field in messages.yml: \"" + field.getName() + "\"");
            }
        }
    }

    public static String getOrDefault(String path, String standard) {

        if (config != null && config.isString(path)) {
            return Prefix + ChatColor.translateAlternateColorCodes('&', config.getString(path));
        } else if (config != null && config.isString(standard)) {
            return Prefix + ChatColor.translateAlternateColorCodes('&', config.getString(standard));
        } else {
            return null;
        }
    }

}
