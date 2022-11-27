package org.ryuu.gdx;

import static org.ryuu.gdx.Screen.SCREEN;

public class GdxApplication extends com.badlogic.gdx.Game {
    @Override
    public void create() {
        setScreen(SCREEN);
    }
}