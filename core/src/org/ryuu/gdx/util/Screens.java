package org.ryuu.gdx.util;

import com.badlogic.gdx.scenes.scene2d.Actor;

import static org.ryuu.gdx.GameApplication.GAME_APPLICATION;

public class Screens {
    public static <T extends Actor> T setSizeAsStage(T actor) {
        actor.setSize(GAME_APPLICATION.getStage().getWidth(), GAME_APPLICATION.getStage().getHeight());
        return actor;
    }
}