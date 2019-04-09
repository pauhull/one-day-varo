package de.pauhull.onedayvaro.util;

import de.pauhull.onedayvaro.scenario.Scenario;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul
 * on 04.04.2019
 *
 * @author pauhull
 */
public class Options {

    @Getter
    @Setter
    private int teamSize;

    @Getter
    @Setter
    private List<Scenario> scenarios;

    @Getter
    @Setter
    private ProtectionPeriod protectionPeriod;

    @Getter
    @Setter
    private World world;

    @Getter
    @Setter
    private boolean nether;

    @Getter
    @Setter
    private int border;

    public Options() {
        this.teamSize = 1;
        this.scenarios = new ArrayList<>();
        this.protectionPeriod = ProtectionPeriod.FIVE_MINUTES;
        this.world = null;
        this.nether = true;
        this.border = 1500;
    }

    public enum ProtectionPeriod {

        NONE(0, 0, "keine"),
        FIVE_MINUTES(1, 5 * 60, "5 Minuten"),
        TEN_MINUTES(2, 10 * 60, "10 Minuten"),
        TWENTY_MINUTES(3, 20 * 60, "20 Minuten"),
        THIRTY_MINUTES(4, 30 * 60, "30 Minuten");

        @Getter
        private int id;

        @Getter
        private int seconds;

        @Getter
        private String name;

        ProtectionPeriod(int id, int seconds, String name) {
            this.id = id;
            this.seconds = seconds;
            this.name = name;
        }

        public static ProtectionPeriod getById(int id) {

            for (ProtectionPeriod protectionPeriod : values()) {
                if (protectionPeriod.getId() == id) {
                    return protectionPeriod;
                }
            }

            return null;
        }

    }

}
