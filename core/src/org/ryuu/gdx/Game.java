package org.ryuu.gdx;

import lombok.Getter;
import lombok.Setter;

import static org.ryuu.gdx.Orientation.Portrait;
import static org.ryuu.gdx.Screen.SCREEN;

public class Game extends com.badlogic.gdx.Game {
    public static final Game GAME_APPLICATION = new Game();
    @Getter
    @Setter
    private static Orientation orientation = Portrait;

    private Game() {
    }

    @Override
    public void create() {
        setScreen(SCREEN);
    }
}