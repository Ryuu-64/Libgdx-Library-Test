package org.ryuu.gdx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Getter;
import org.ryuu.gdx.scenes.scene2d.utils.ActionUtil;
import org.ryuu.gdx.test.SoundManager;

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
        getStage().setDebugAll(true);

        Group group = getStageScreen().setSizeAsStage(new Group());
        getStage().addActor(group);

        Image image = new Image(new Texture(badlogic_jpg));
        group.addActor(image);
        align(image, center);

        assetManager = new AssetManager();
        assetManager.load($assets.sfx.hit_mp3, Sound.class);
        assetManager.load($assets.sfx.button_mp3, Sound.class);
        assetManager.load($assets.sfx.goal_mp3, Sound.class);
        assetManager.finishLoading();

        soundManager = new SoundManager(10, (path) -> assetManager.get(path, Sound.class));
        for (int i = 0; i < 10; i++) {
            getStage().addAction(ActionUtil.delay(1, () -> {
                SoundManager.SoundWrapper play = soundManager.play($assets.sfx.button_mp3);
                getStage().addAction(ActionUtil.delay(1, () -> System.out.println(play)));
            }));
        }
    }

    public StageScreen getStageScreen() {
        return (StageScreen) getScreen();
    }

    public Stage getStage() {
        return ((StageScreen) getScreen()).getStage();
    }
}