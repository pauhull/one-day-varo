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
    public static String SpawnAdd = "";
    public static String TeamSizeChanged = "";

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
            }
        }
    }

}
