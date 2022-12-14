package org.ryuu.gdx.test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import static org.ryuu.gdx.GameApplication.GAME_APPLICATION;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("ViewPortTest");
        config.setWindowedMode(1920, 1080);
        new Lwjgl3Application(GAME_APPLICATION, config);
    }
}