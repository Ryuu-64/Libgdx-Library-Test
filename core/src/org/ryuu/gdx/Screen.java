package org.ryuu.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.Getter;

import static com.badlogic.gdx.Gdx.files;
import static org.ryuu.gdx.Orientation.Landscape;
import static org.ryuu.gdx.Orientation.Portrait;

public class Screen extends ScreenAdapter {
    public static final Screen SCREEN = new Screen();
    @Getter
    private final OrthographicCamera orthographicCamera;
    @Getter
    private final ExtendViewport extendViewport;
    @Getter
    private final Stage stage;
    @Getter
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private Screen() {
        orthographicCamera = new OrthographicCamera();
        extendViewport = new ExtendViewport(ScreenSettings.getDesignWorldWidth(), ScreenSettings.getDesignWorldHeight(), orthographicCamera);
        stage = new Stage(extendViewport);
        stage.setDebugAll(GameSettings.isStageSetDebugAll());
        inputMultiplexer.addProcessor(stage);
        Image background = new Image(new Texture(files.internal("badlogic.jpg")));
        background.setSize(ScreenSettings.getDesignWorldWidth(), ScreenSettings.getDesignWorldHeight());
        stage.addActor(background);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        int designWorldWidth = ScreenSettings.getDesignWorldWidth();
        int designWorldHeight = ScreenSettings.getDesignWorldHeight();
        if (Game.getOrientation() == Portrait && GameSettings.getDefaultOrientation() != Portrait) {
            designWorldWidth = ScreenSettings.getDesignWorldHeight();
            designWorldHeight = ScreenSettings.getDesignWorldWidth();
        } else if (Game.getOrientation() == Landscape && GameSettings.getDefaultOrientation() != Landscape) {
            designWorldWidth = ScreenSettings.getDesignWorldHeight();
            designWorldHeight = ScreenSettings.getDesignWorldWidth();
        }
        extendViewport.setMinWorldWidth(designWorldWidth);
        extendViewport.setMinWorldHeight(designWorldHeight);
        extendViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}