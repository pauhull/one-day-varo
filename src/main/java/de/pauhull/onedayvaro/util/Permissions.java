package de.pauhull.onedayvaro.util;

import de.pauhull.onedayvaro.OneDayVaro;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class Permissions {

    public static String SetLocation;

    public static void load() {

        load(OneDayVaro.getInstance().copyAndLoad("permissions.yml", new File(OneDayVaro.getInstance().getDataFolder(), "permissions.yml")));
    }

    public static void load(FileConfiguration config) {

        for (Field field : Permissions.class.getDeclaredFields()) {
            if (config.isString(field.getName())) {
                try {
                    field.set(null, config.getString(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}