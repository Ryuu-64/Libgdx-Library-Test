package org.ryuu.gdx.settings;

import lombok.Getter;
import lombok.Setter;

public class ScreenSettings {
    private ScreenSettings() {
    }

    @Getter
    @Setter
    private static int designWorldWidth = 1920;
    @Getter
    @Setter
    private static int designWorldHeight = 1080;
}