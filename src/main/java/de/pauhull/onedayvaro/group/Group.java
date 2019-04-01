package de.pauhull.onedayvaro.group;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
@Builder
public class Group {

    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private String chatFormat;
    @Getter
    private String tablistPrefix;
    @Getter
    private String scoreboardName;
    @Getter
    private String permission;

}
