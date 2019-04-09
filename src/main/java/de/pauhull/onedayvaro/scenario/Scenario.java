package de.pauhull.onedayvaro.scenario;

import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul
 * on 04.04.2019
 *
 * @author pauhull
 */
public abstract class Scenario {

    @Getter
    protected static List<Scenario> allScenarios = new ArrayList<>();

    public static <T extends Scenario> T getInstance(Class<T> scenarioClass) {

        for (Scenario scenario : allScenarios) {
            if (scenario.getClass().equals(scenarioClass)) {
                return scenarioClass.cast(scenario);
            }
        }

        return null;
    }

    public abstract List<String> getLore();

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean enabled);

    public abstract Material getMaterial();

    public abstract String getDisplayName();

    public abstract short getDurability();

}
