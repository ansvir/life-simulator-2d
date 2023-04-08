package com.itique.ls2d.custom.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.itique.ls2d.custom.component.style.Style;

public class MapNavigationListener extends AbstractListener {

    private OrthographicCamera camera;
    private float zoomSpeed;
    private float panSpeed;
    private float screenWidth, screenHeight;
    private float zoom;
    private boolean moveLeft, moveRight, moveUp, moveDown;

    public MapNavigationListener(OrthographicCamera camera, Stage stage, Style style) {
        super(stage, style);
        this.camera = camera;
        this.zoomSpeed = 0.1f;
        this.panSpeed = 50f;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.zoom = 1f;
    }

    @Override
    public boolean keyDown(int keycode) {
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
    public boolean scrolled(float screenX, float screenY) {
        zoom += screenY * zoomSpeed;
        zoom = MathUtils.clamp(zoom, 0.1f, 10f);
        camera.zoom = zoom;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float margin = 50f;
        if (screenX < margin) {
            camera.translate(-panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
        }
        if (screenX > screenWidth - margin) {
            camera.translate(panSpeed * Gdx.graphics.getDeltaTime() * zoom, 0);
        }
        if (screenY < margin) {
            camera.translate(0, panSpeed * Gdx.graphics.getDeltaTime() * zoom);
        }
        if (screenY > screenHeight - margin) {
            camera.translate(0, -panSpeed * Gdx.graphics.getDeltaTime() * zoom);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
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
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

}
