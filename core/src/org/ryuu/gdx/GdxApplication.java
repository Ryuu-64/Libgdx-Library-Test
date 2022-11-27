package org.ryuu.gdx;

import static org.ryuu.gdx.Screen.SCREEN;

public class GdxApplication extends com.badlogic.gdx.Game {
    public static final GdxApplication GDX_APPLICATION = new GdxApplication();

    private GdxApplication() {
    }

    @Override
    public void create() {
        setScreen(SCREEN);
    }
}