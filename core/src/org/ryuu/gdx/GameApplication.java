package org.ryuu.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Getter;
import org.ryuu.gdx.scenes.scene2d.ui.VisibleVerticalGroup;
import org.ryuu.gdx.scenes.scene2d.utils.ActionUtil;
import org.ryuu.gdx.audio.SoundManager;
import org.ryuu.gdx.util.Screens;
import org.ryuu.popup.PopUpEvent;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*;
import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;
import static org.ryuu.gdx.$assets.texture2d.badlogic_jpg;
import static org.ryuu.gdx.scenes.scene2d.utils.Actors.align;
import static org.ryuu.gdx.util.Screens.*;

public class GameApplication extends Game {
    public static final GameApplication GAME_APPLICATION = new GameApplication();
    @Getter
    private static AssetManager assetManager;
    @Getter
    private static SoundManager soundManager;

    private GameApplication() {
    }

    @Override
    public void create() {
        setScreen(new StageWorldScreen(1920, 1080, new StageWorldScreen.WorldSettings(
                120, .02f, 4, 4
        )));
        visibleVerticalGroupTest();
    }

    public StageWorldScreen getStageWorldScreen() {
        return (StageWorldScreen) getScreen();
    }

    public Stage getStage() {
        return ((StageWorldScreen) getScreen()).getStage();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        getStageWorldScreen().dispose();
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

        Group group = setSizeAsStage(new Group());
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

    public void box2dWorldTest() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getStageWorldScreen().worldUnitToMeter(getStage().getWidth() / 2), getStageWorldScreen().worldUnitToMeter(getStage().getHeight() / 2));
        bodyDef.type = StaticBody;
        Body body = getStageWorldScreen().getWorld().createBody(bodyDef);
        MassData massData = new MassData();
        massData.mass = Integer.MAX_VALUE;
        body.setMassData(massData);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(4.5f);
        body.createFixture(circleShape, 1);
        circleShape.dispose();
    }

    public void visibleVerticalGroupTest() {
        Group group = setSizeAsStage(new Group());
        getStage().addActor(group);

        VisibleVerticalGroup visibleVerticalGroup = new VisibleVerticalGroup();
        group.addActor(visibleVerticalGroup);
        visibleVerticalGroup.space(5);

        Image image1 = new Image(new Texture(badlogic_jpg));
        visibleVerticalGroup.addActor(image1);

        Image image2 = new Image(new Texture(badlogic_jpg));
        visibleVerticalGroup.addActor(image2);
        image2.setColor(new Color(1, 1, 1, .75f));
        image2.setVisible(false);

        Image image3 = new Image(new Texture(badlogic_jpg));
        visibleVerticalGroup.addActor(image3);
        image3.setColor(new Color(1, 1, 1, .5f));

        visibleVerticalGroup.pack();
        align(visibleVerticalGroup, center);

        image2.addAction(ActionUtil.foreverDelay(() -> {
            image2.setVisible(!image2.isVisible());
            visibleVerticalGroup.invalidate();
            align(visibleVerticalGroup, top);
        }, .25f));

        visibleVerticalGroup.top();
        visibleVerticalGroup.setDebug(true, true);
    }
}