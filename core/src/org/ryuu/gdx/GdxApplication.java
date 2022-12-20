package org.ryuu.gdx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Getter;
import org.ryuu.gdx.scenes.scene2d.utils.ActionUtil;
import org.ryuu.gdx.audio.SoundManager;
import org.ryuu.popup.PopUpEvent;

import static com.badlogic.gdx.utils.Align.center;
import static org.ryuu.gdx.$assets.texture2d.badlogic_jpg;
import static org.ryuu.gdx.scenes.scene2d.utils.Actors.align;

public class GdxApplication extends com.badlogic.gdx.Game {
    @Getter
    private static AssetManager assetManager;
    @Getter
    private static SoundManager soundManager;

    @Override
    public void create() {
        setScreen(new StageScreen(1920, 1080));
        popUpTest();
        soundMangerTest();
    }

    public StageScreen getStageScreen() {
        return (StageScreen) getScreen();
    }

    public Stage getStage() {
        return ((StageScreen) getScreen()).getStage();
    }

    public void soundMangerTest() {
        assetManager = new AssetManager();
        assetManager.load($assets.sfx.button_mp3, Sound.class);
        assetManager.load($assets.sfx.win_mp3, Music.class);
        assetManager.finishLoading();
        soundManager = new SoundManager(10, (path) -> assetManager.get(path, Sound.class));
        for (int i = 0; i < 10; i++) {
            getStage().addAction(ActionUtil.delay(i, () -> soundManager.play($assets.sfx.button_mp3, 1, 1, 1)));
        }
    }

    public void popUpTest() {
        PopUpEvent popUpEvent = new PopUpEvent();

        Group group = getStageScreen().setSizeAsStage(new Group());
        getStage().addActor(group);

        popUpEvent.add(1, popUp -> popUp.getOnDispose().invoke());
        popUpEvent.add(2, popUp -> {
            Image logo = new Image(new Texture(badlogic_jpg));
            group.addActor(logo);
            align(logo, center);
            popUp.getOnDispose().invoke();
            logo.addAction(Actions.sequence(
                    Actions.moveBy(-750, 0, 1),
                    ActionUtil.of(() -> popUp.getOnDispose().invoke())
            ));
        });
        popUpEvent.add(2, popUp -> {
            Image logo = new Image(new Texture(badlogic_jpg));
            group.addActor(logo);
            align(logo, center);
            popUp.getOnDispose().invoke();
            logo.addAction(Actions.sequence(
                    Actions.moveBy(750, 0, 1),
                    ActionUtil.of(() -> popUp.getOnDispose().invoke())
            ));
        });
        popUpEvent.add(3, popUp -> {
            Image logo = new Image(new Texture(badlogic_jpg));
            group.addActor(logo);
            align(logo, center);
            logo.addAction(Actions.sequence(
                    Actions.moveBy(0, 250, 1),
                    ActionUtil.of(() -> popUp.getOnDispose().invoke())
            ));
        });
        popUpEvent.add(3, popUp -> {
            Image logo = new Image(new Texture(badlogic_jpg));
            group.addActor(logo);
            align(logo, center);
            logo.addAction(Actions.sequence(
                    Actions.moveBy(0, -250, 1),
                    ActionUtil.of(() -> popUp.getOnDispose().invoke())
            ));
        });

        popUpEvent.invoke();
    }
}