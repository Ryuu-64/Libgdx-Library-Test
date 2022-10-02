package org.ryuu.gdx;

import lombok.Getter;
import lombok.Setter;

public class ScreenSettings {
    private ScreenSettings() {
    }

    @Getter
    @Setter
    private static int designWorldWidth = 540;
    @Getter
    @Setter
    private static int designWorldHeight = 960;
}