package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class MapActor extends Group {

    private float borderWidth = 5f;
    private Color borderColor = Color.RED;
    private Texture backgroundTexture;
    private float width, height;
    private ArrayList<AbstractMapGroup> objects;
    private OrthographicCamera camera;
    private float zoomSpeed;
    private float panSpeed;
    private float zoom;
    private boolean moveLeft, moveRight, moveUp, moveDown;

    public MapActor(Texture backgroundTexture, float width, float height, OrthographicCamera camera) {
        this.backgroundTexture = backgroundTexture;
        this.width = width;
        this.height = height;
        this.objects = new ArrayList<>();
        this.camera = camera;
        this.zoomSpeed = 0.1f;
        this.panSpeed = 50f;
        this.zoom = 1f;
        setSize(width, height);
        setOrigin(Align.center);
        addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                zoom += amountY * zoomSpeed;
                zoom = MathUtils.clamp(zoom, 0.1f, 10f);
                camera.zoom = zoom;
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                float delta = Gdx.graphics.getDeltaTime();
                if (keycode == Input.Keys.A) {
                    camera.translate(-panSpeed * delta * zoom, 0);
                    moveLeft = true;
                }
                if (keycode == Input.Keys.D) {
                    camera.translate(panSpeed * delta * zoom, 0);
                    moveRight = true;
                }
                if (keycode == Input.Keys.W) {
                    camera.translate(0, panSpeed * delta * zoom);
                    moveUp = true;
                }
                if (keycode == Input.Keys.S) {
                    camera.translate(0, -panSpeed * delta * zoom);
                    moveDown = true;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.A:
                        moveLeft = false;
                        break;
                    case Input.Keys.D:
                        moveRight = false;
                        break;
                    case Input.Keys.W:
                        moveUp = false;
                        break;
                    case Input.Keys.S:
                        moveDown = false;
                        break;
                }
                return true;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                float margin = 50f;
                if (x < margin) {
                    camera.translate(-panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
                }
                if (x > width - margin) {
                    camera.translate(panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
                }
                if (x < margin) {
                    camera.translate(0, panSpeed * Gdx.graphics.getDeltaTime() * zoom);
                }
                if (x > height - margin) {
                    camera.translate(0, -panSpeed * Gdx.graphics.getDeltaTime() * zoom);
                }
                return true;
            }
        });
    }

    public void addObject(AbstractMapGroup object) {
        objects.add(object);
        addActor(object);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        batch.draw(backgroundTexture, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
        batch.end();

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setColor(borderColor);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.identity();
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(getX() - borderWidth, getY() - borderWidth, getWidth() + borderWidth * 2, getHeight() + borderWidth * 2);
        shapeRenderer.rectLine(getX(), getY(), getX(), getY() + getHeight(), borderWidth);
        shapeRenderer.rectLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight(), borderWidth);
        shapeRenderer.rectLine(getX(), getY(), getX() + getWidth(), getY(), borderWidth);
        shapeRenderer.rectLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight(), borderWidth);
        shapeRenderer.end();

        batch.begin();
    }

}
