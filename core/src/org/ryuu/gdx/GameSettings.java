package org.ryuu.gdx;

import lombok.Getter;
import lombok.Setter;

import static org.ryuu.gdx.Orientation.Portrait;

public class GameSettings {
    private GameSettings() {
    }

    @Getter
    @Setter
    private static Orientation defaultOrientation = Portrait;
    @Getter
    @Setter
    private static boolean isStageSetDebugAll = true;
}