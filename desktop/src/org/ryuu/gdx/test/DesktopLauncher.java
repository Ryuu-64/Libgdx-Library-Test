package org.ryuu.gdx.test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.ryuu.gdx.Game;
import org.ryuu.gdx.GameSettings;
import org.ryuu.gdx.Orientation;
import org.ryuu.gdx.ScreenSettings;

import static org.ryuu.gdx.Game.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("ViewPortTest");
        config.setWindowedMode(1080, 960);
        ScreenSettings.setDesignWorldWidth(540);
        ScreenSettings.setDesignWorldHeight(960);
        GameSettings.setDefaultOrientation(Orientation.Portrait);
        Game.setOrientation(Orientation.Portrait);
        new Lwjgl3Application(GAME_APPLICATION, config);
    }
}