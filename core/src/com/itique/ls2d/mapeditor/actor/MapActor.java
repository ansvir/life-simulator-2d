package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class MapActor extends Group implements Disposable {

    private static final int BORDER_DIAPASON = 2;
    private Texture backgroundTexture;
    private float width, height;
    private ArrayList<AbstractMapGroup> objects;
    private OrthographicCamera camera;
    private float zoomSpeed;
    private float panSpeed;
    private float zoom;
    private Texture bordersTexture;
    private Pixmap borders;
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
        borders = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        bordersTexture = new Texture(borders);
        addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                zoom += amountY * zoomSpeed;
                zoom = MathUtils.clamp(zoom, 0.1f, 10f);
                camera.zoom = zoom;
                return false;
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
                return false;
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
                return false;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                float margin = 50f;
                float width = Gdx.graphics.getWidth();
                float height = Gdx.graphics.getHeight();
                if (x < margin) {
                    camera.translate(-panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
                }
                if (x > width - margin) {
                    camera.translate(panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
                }
                if (y < margin) {
                    camera.translate(0, panSpeed * Gdx.graphics.getDeltaTime() * zoom);
                }
                if (y > height - margin) {
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
        batch.draw(drawBorders(), 0, 0);
        super.draw(batch, parentAlpha);
        batch.end();
        batch.begin();
    }

    private Texture drawBorders() {
        borders.dispose();
        borders = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        borders.setColor(Color.RED);
        int dotSize = (int) (camera.zoom * 3f);
        for (int y = -BORDER_DIAPASON * dotSize; y <= backgroundTexture.getHeight(); y += BORDER_DIAPASON * dotSize) {
            borders.fillRectangle(0, y, dotSize, dotSize);
        }
        for (int y = -BORDER_DIAPASON * dotSize; y <= backgroundTexture.getHeight(); y += BORDER_DIAPASON * dotSize) {
            borders.fillRectangle(backgroundTexture.getWidth() - dotSize, y, dotSize, dotSize);
        }
        for (int x = -BORDER_DIAPASON * dotSize; x <= backgroundTexture.getWidth(); x += BORDER_DIAPASON * dotSize) {
            borders.fillRectangle(x, 0, dotSize, dotSize);
        }
        for (int x = -BORDER_DIAPASON * dotSize; x <= backgroundTexture.getWidth(); x += BORDER_DIAPASON * dotSize) {
            borders.fillRectangle(x, backgroundTexture.getHeight() - dotSize, dotSize, dotSize);
        }
        bordersTexture.dispose();
        bordersTexture = new Texture(borders);
        return bordersTexture;
    }

    @Override
    public void dispose() {
        bordersTexture.dispose();
        borders.dispose();
    }
}
