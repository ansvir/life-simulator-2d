package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class MapActor extends Group {

    private static final int BORDER_DIAPASON = 5;
    private Texture backgroundTexture;
    private float width, height;
    private ArrayList<AbstractMapGroup> objects;
    private OrthographicCamera camera;
    private float zoomSpeed;
    private float panSpeed;
    private float zoom;
    private boolean moveLeft, moveRight, moveUp, moveDown;
    private float currX, currY;

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
        drawMapBorders();
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
                boolean isMoved = false;
                if (keycode == Input.Keys.A) {
                    camera.translate(-panSpeed * delta * zoom, 0);
                    moveLeft = true;
                    isMoved = true;
                }
                if (keycode == Input.Keys.D) {
                    camera.translate(panSpeed * delta * zoom, 0);
                    moveRight = true;
                    isMoved = true;
                }
                if (keycode == Input.Keys.W) {
                    camera.translate(0, panSpeed * delta * zoom);
                    moveUp = true;
                    isMoved = true;
                }
                if (keycode == Input.Keys.S) {
                    camera.translate(0, -panSpeed * delta * zoom);
                    moveDown = true;
                    isMoved = true;
                }
                if (isMoved) {
                    drawMapBorders();
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
                drawMapBorders();
                return false;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                float margin = 50f;
                float width = Gdx.graphics.getWidth();
                float height = Gdx.graphics.getHeight();
                boolean isMoved = false;
                if (x < margin) {
                    camera.translate(-panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
                    isMoved = true;
                }
                if (x > width - margin) {
                    camera.translate(panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
                    isMoved = true;
                }
                if (y < margin) {
                    camera.translate(0, panSpeed * Gdx.graphics.getDeltaTime() * zoom);
                    isMoved = true;
                }
                if (y > height - margin) {
                    camera.translate(0, -panSpeed * Gdx.graphics.getDeltaTime() * zoom);
                    isMoved = true;
                }
                if (isMoved) {
                    drawMapBorders();
                }
                return false;
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
        Texture texture = new Texture("textures/book.png");
        batch.draw(texture, getWidth() / 2, getHeight() / 2);
        super.draw(batch, parentAlpha);
        batch.end();
        batch.begin();
    }

    public void drawMapBorders() {
        if (!backgroundTexture.getTextureData().isPrepared()) {
            backgroundTexture.getTextureData().prepare();
        }
        Pixmap pixmap = backgroundTexture.getTextureData().consumePixmap();
        pixmap.setColor(Color.RED);
        for (int y = 0; y <= backgroundTexture.getHeight(); y += BORDER_DIAPASON) {
            int drawY = y;
            pixmap.drawPixel(0, drawY++);
            pixmap.drawPixel(0, drawY);
        }
        for (int y = 0; y <= backgroundTexture.getHeight(); y += BORDER_DIAPASON) {
            int drawY = y;
            pixmap.drawPixel(backgroundTexture.getWidth() - 1, drawY++);
            pixmap.drawPixel(backgroundTexture.getWidth() - 1, drawY);
        }
        for (int x = 0; x <= backgroundTexture.getHeight(); x += BORDER_DIAPASON) {
            int drawX = x;
            pixmap.drawPixel(drawX++, 0);
            pixmap.drawPixel(drawX, 0);
        }
        for (int x = 0; x <= backgroundTexture.getHeight(); x += BORDER_DIAPASON) {
            int drawX = x;
            pixmap.drawPixel(drawX++, backgroundTexture.getHeight() - 1);
            pixmap.drawPixel(drawX, backgroundTexture.getHeight() - 1);
        }
        backgroundTexture = new Texture(pixmap);
    }

}
