package org.ryuu.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.ryuu.gdx.graphics.glutils.Material;
import org.ryuu.gdx.scenes.scene2d.MaterialGroup;
import org.ryuu.gdx.scenes.scene2d.utils.Actors;

import static com.badlogic.gdx.Gdx.files;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static org.ryuu.gdx.Screen.*;
import static org.ryuu.gdx.graphics.glutils.utils.Shaders.*;
import static org.ryuu.gdx.scenes.scene2d.utils.Actors.align;
import static org.ryuu.gdx.scenes.scene2d.utils.ClickListeners.addColorChange;
import static org.ryuu.gdx.scenes.scene2d.utils.ClickListeners.addDownUpSizeChange;

public class MaterialGroupTest extends Group {
    public static void add() {
        Group group = stageSizeGroup();
        for (int i = 0; i < 5; i++) {
            MaterialGroup materialGroup = materialGroup();
            group.addActor(materialGroup);
            align(materialGroup, left, i * 400, 0);
            Material material = new Material();
            materialGroup.setMaterial(material);
            material.setShaderProgram(HDR);
            material.setAttributef(HDR_COLOR_ATTRIBUTE, 1, 1, 1, 1);
            material.setAttributef(INTENSITY_ATTRIBUTE, 1 / (i + 1f), 0, 0, 0);
            addColorChange(materialGroup, WHITE, (i + 1) * 2);
            materialGroup.setOrigin(center);
            addDownUpSizeChange(materialGroup);
        }
        SCREEN.getStage().addActor(group);
    }

    private static MaterialGroup materialGroup() {
        MaterialGroup group = new MaterialGroup();
        Image background = new Image(new Texture(files.internal("whitePixel.png")));
        background.setSize(300, 300);
        Actors.setSize(group, background);
        group.addActor(background);
        Image logo = new Image(new Texture(files.internal("badlogic.jpg")));
        group.addActor(logo);
        align(logo, center);
        return group;
    }

    private static Group stageSizeGroup() {
        Group group = new Group();
        Stage stage = SCREEN.getStage();
        group.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(group);
        return group;
    }
}