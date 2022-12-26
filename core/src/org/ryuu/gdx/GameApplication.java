package org.ryuu.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Getter;
import org.ryuu.gdx.graphics.glutils.Material;
import org.ryuu.gdx.scenes.scene2d.ui.MaterialImage;
import org.ryuu.gdx.scenes.scene2d.ui.VisibleVerticalGroup;
import org.ryuu.gdx.scenes.scene2d.utils.ActionUtil;
import org.ryuu.gdx.audio.SoundManager;
import org.ryuu.gdx.util.Screens;
import org.ryuu.popup.PopUpEvent;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*;
import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;
import static org.ryuu.gdx.$assets.texture2d.badlogic_jpg;
import static org.ryuu.gdx.$assets.texture2d.blur_png;
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
        gaussianBlurTest();
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

    public void gaussianBlurTest() {
        Group group = setSizeAsStage(new Group());
        getStage().addActor(group);

        MaterialImage materialImage = new MaterialImage(new Texture(blur_png));
        group.addActor(materialImage);
        align(materialImage, center);

        Material material = new Material();
        materialImage.setMaterial(material);
        material.setShaderProgram(new ShaderProgram(
                "attribute vec4 a_position;\n" +
                        "attribute vec4 a_color;\n" +
                        "attribute vec2 a_texCoord0;\n" +
                        "attribute vec2 a_resolution;\n" +
                        "uniform mat4 u_projTrans;\n" +
                        "varying vec4 v_color;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "varying vec2 v_resolution;\n" +
                        "\n" +
                        "void main() {\n" +
                        "    v_color = a_color;\n" +
                        "    v_texCoords = a_texCoord0;\n" +
                        "    v_resolution = a_resolution;\n" +
                        "    gl_Position = u_projTrans * a_position;\n" +
                        "}",
                "#ifdef GL_ES\n" +
                        "precision mediump float;\n" +
                        "#endif\n" +
                        "\n" +
                        "varying vec4 v_color;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "varying vec2 v_resolution;\n" +
                        "uniform sampler2D u_texture;\n" +
                        "\n" +
                        "float normpdf(in float x, in float sigma) {\n" +
                        "    return 0.39894 * exp(-0.5 * x * x / (sigma * sigma)) / sigma;\n" +
                        "}\n" +
                        "\n" +
                        "vec3 gaussianBlur(sampler2D texture) {\n" +
                        "    const int mSize = 256;// TODO core size\n" +
                        "    const int kSize = (mSize-1)/2;\n" +
                        "    float kernel[mSize];\n" +
                        "    vec3 finalColor = vec3(0.0);\n" +
                        "\n" +
                        "    // create the 1-D kernel\n" +
                        "    float sigma = 7.0;\n" +
                        "    float Z = 0.0;\n" +
                        "    for (int i = 0; i <= kSize; i++)\n" +
                        "    {\n" +
                        "        kernel[kSize + i] = kernel[kSize - i] = normpdf(float(i), sigma);\n" +
                        "    }\n" +
                        "\n" +
                        "    // get the normalization factor (as the gaussian has been clamped)\n" +
                        "    for (int i = 0; i < mSize; i++)\n" +
                        "    {\n" +
                        "        Z += kernel[i];\n" +
                        "    }\n" +
                        "\n" +
                        "//     read out the texels\n" +
                        "        for (int i = -kSize; i <= kSize; i++)\n" +
                        "        {\n" +
                        "                finalColor += kernel[kSize + i] * texture2D(u_texture, v_texCoords.xy + vec2(float(i), 0.0) / v_resolution).rgb;\n" +
                        "        }\n" +
                        "        for (int j = -kSize; j <= kSize; j++)\n" +
                        "        {\n" +
//                        "                finalColor += kernel[kSize + j] * texture2D(u_texture, v_texCoords.xy + vec2(0.0, float(j)) / v_resolution).rgb;\n" +
                        "        }\n" +
                        "\n" +
                        "    return finalColor ;\n" +
                        "}\n" +
                        "\n" +
                        "void main() {\n" +
                        "    gl_FragColor = vec4(gaussianBlur(u_texture), 1) * v_color;\n" +
                        "}"
        ));
        if (!material.getShaderProgram().isCompiled()) {
            Gdx.app.log("", material.getShaderProgram().getLog());
        }
        material.setAttributef("a_resolution", materialImage.getWidth(), materialImage.getHeight(), 0, 0);
    }
}