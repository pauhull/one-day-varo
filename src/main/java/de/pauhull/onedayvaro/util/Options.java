package de.pauhull.onedayvaro.util;

import de.pauhull.onedayvaro.scenario.Scenario;
import lombok.Getter;
import lombok.Setter;

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

    public Options() {
        this.teamSize = 1;
        this.scenarios = new ArrayList<>();
    }

}
