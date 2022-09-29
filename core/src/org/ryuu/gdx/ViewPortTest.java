package org.ryuu.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import org.ryuu.gdx.graphics.glutils.Material;
import org.ryuu.gdx.scenes.scene2d.MaterialGroup;

import static com.badlogic.gdx.Gdx.files;
import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.Color.*;
import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static org.ryuu.gdx.graphics.glutils.utils.Shaders.*;
import static org.ryuu.gdx.scenes.scene2d.utils.Actors.align;
import static org.ryuu.gdx.scenes.scene2d.utils.Actors.setSize;
import static org.ryuu.gdx.scenes.scene2d.utils.ClickListeners.addColorChange;
import static org.ryuu.gdx.CoordinateManager.DESIGN_RESOLUTION;

public class ViewPortTest extends ApplicationAdapter {
    @Getter
    private OrthographicCamera camera;
    @Getter
    private Viewport viewport;
    @Getter
    private Stage stage;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(DESIGN_RESOLUTION.x, DESIGN_RESOLUTION.y, camera);
        stage = new Stage(viewport);
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
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private MaterialGroup materialGroup() {
        MaterialGroup group = new MaterialGroup();
        Image background = new Image(new Texture(files.internal("whitePixel.png")));
        background.setSize(300, 300);
        setSize(group, background);
        group.addActor(background);
        Image logo = new Image(new Texture(files.internal("badlogic.jpg")));
        group.addActor(logo);
        align(logo, center);
        return group;
    }

    private Group stageSizeGroup() {
        Group group = new Group();
        group.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(group);
        return group;
    }
}