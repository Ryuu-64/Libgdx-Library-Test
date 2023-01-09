package org.ryuu.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.Getter;
import org.ryuu.gdx.audio.SoundThreadPoolExecutor;
import org.ryuu.gdx.scenes.scene2d.ui.VisibleVerticalGroup;
import org.ryuu.gdx.scenes.scene2d.utils.ActionUtil;
import org.ryuu.popup.PopUpEvent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;
import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;
import static org.ryuu.gdx.$assets.fnt;
import static org.ryuu.gdx.$assets.sfx;
import static org.ryuu.gdx.$assets.shader.gaussianBlur_frag;
import static org.ryuu.gdx.$assets.shader.gaussianBlur_vert;
import static org.ryuu.gdx.$assets.texture2d.badlogic_jpg;
import static org.ryuu.gdx.$assets.texture2d.whitePixel_png;
import static org.ryuu.gdx.scenes.scene2d.utils.Actors.align;
import static org.ryuu.gdx.scenes.scene2d.utils.Actors.setSize;
import static org.ryuu.gdx.util.Screens.setSizeAsStage;

public class GameApplication extends Game {
    public static final GameApplication GAME_APPLICATION = new GameApplication();
    @Getter
    private static AssetManager assetManager;
    @Getter
    private static SoundThreadPoolExecutor soundThreadPoolExecutor;

    private GameApplication() {
    }

    @Override
    public void create() {
        setScreen(new StageWorldScreen(1920, 1080, new StageWorldScreen.WorldSettings(
                120, .02f, 4, 4
        )));
        soundMangerTest();
//        fntTest();
    }

    public StageWorldScreen getStageWorldScreen() {
        return (StageWorldScreen) getScreen();
    }

    public Stage getStage() {
        return ((StageWorldScreen) getScreen()).getStage();
    }

    @Override
    public void dispose() {
        super.dispose();
        getStageWorldScreen().dispose();
    }

    public void fntTest() {
        Group group = setSizeAsStage(new Group());
        getStage().addActor(group);

        Image background = new Image(new Texture(whitePixel_png));
        group.addActor(background);
        setSize(background, group);
        background.setColor(Color.RED);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal(fnt.newVarietyShowW9P64_fnt), Gdx.files.internal(fnt.newVarietyShowW9P64_png), false);
        labelStyle.fontColor = Color.WHITE;

        TestLabel label = new TestLabel(
                "1234567890\n" +
                        "abcdefghijklmnopqrstuvwxyz",
                labelStyle
        );
        group.addActor(label);
        align(label, center);
    }

    private static class TestLabel extends Label {
        private final FrameBuffer frameBuffer;
        private final TextureRegion textureRegion = new TextureRegion();
        private final TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        public TestLabel(CharSequence text, LabelStyle style) {
            super(text, style);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.flush();

            setColor(Color.BLUE);

            frameBuffer.begin();
            Gdx.gl.glClearColor(1, 1, 1, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            super.draw(batch, parentAlpha);
            batch.flush();
            frameBuffer.end();

            Texture bufferTexture = frameBuffer.getColorBufferTexture();
            textureRegion.setTexture(bufferTexture);
            textureRegion.setRegion(0, 0, bufferTexture.getWidth(), bufferTexture.getHeight());
            textureRegion.flip(false, true);

            ShaderProgram batchShader = batch.getShader();
            batch.setShader(new ShaderProgram(Gdx.files.internal(gaussianBlur_vert).readString("UTF-8"), Gdx.files.internal(gaussianBlur_frag).readString("UTF-8")));
            batch.getShader().setAttributef("a_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0);
            textureRegionDrawable.draw(batch, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setShader(batchShader);

            super.draw(batch, parentAlpha);
        }
    }

    public void soundMangerTest() {
        assetManager = new AssetManager();
        assetManager.load(sfx.button_mp3, Sound.class);
        assetManager.load(sfx.win_mp3, Sound.class);
        assetManager.finishLoading();
        soundThreadPoolExecutor = new SoundThreadPoolExecutor(
                new ThreadPoolExecutor(
                        1,
                        1,
                        1,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(1 << 10)
                )
        );
        soundThreadPoolExecutor.play(assetManager.get(sfx.button_mp3, Sound.class));
        SoundThreadPoolExecutor.SoundWrapper play = soundThreadPoolExecutor.play(assetManager.get(sfx.win_mp3, Sound.class));
        Sound sound = play.getSound();

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