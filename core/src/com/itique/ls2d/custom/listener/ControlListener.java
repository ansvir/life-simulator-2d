package com.itique.ls2d.custom.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.model.timeline.TimeLineTask;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.Input.Keys.S;

public class ControlListener implements InputProcessor {

    private static final int DELTA_PX = 20;
    private static final int EDGE_PX = 15;
    private static final float ZOOM_DIVIDER = 5f;
    private static final float ZOOM_MIN = 1.0f / ZOOM_DIVIDER;
    private static final float ZOOM_MAX = ZOOM_MIN * ZOOM_DIVIDER;

    private final Stage stage;
    private final IntSet keys;

    public ControlListener(Stage stage) {
        this.stage = stage;
        keys = new IntSet();
    }

    @Override
    public boolean keyDown(int keycode) {
        Camera camera = stage.getCamera();
        if (!keys.contains(keycode)) {
            keys.add(keycode);
        }
        float x = camera.position.x;
        float y = camera.position.y;
        if (keys.contains(A)) {
            camera.position.lerp(new Vector3(x - DELTA_PX, y, 0), 0.1f);
        }
        if (keys.contains(W)) {
            camera.position.lerp(new Vector3(x, y + DELTA_PX, 0), 0.1f);
        }
        if (keys.contains(D)) {
            camera.position.lerp(new Vector3(x + DELTA_PX, y, 0), 0.1f);
        }
        if (keys.contains(S)) {
            camera.position.lerp(new Vector3(x, y - DELTA_PX, 0), 0.1f);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (this.keys.contains(keycode)) {
            this.keys.remove(keycode);
        }
        return false;
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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        processMapNavigation(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        if (Float.compare(camera.zoom, ZOOM_MAX) >= 0) {
            camera.zoom = ZOOM_MAX - 1.0f / ZOOM_DIVIDER;
        } else if (Float.compare(camera.zoom, ZOOM_MIN) <= 0) {
            camera.zoom = ZOOM_MIN + 1.0f / ZOOM_DIVIDER;
        }
        if (Float.compare(camera.zoom, ZOOM_MAX) < 0
                && Float.compare(camera.zoom, ZOOM_MIN) > 0) {
            camera.zoom += amountY / ZOOM_DIVIDER;
        }
        return false;
    }

    private void processMapNavigation(int screenX, int screenY) {
        Camera camera = stage.getCamera();
        if (screenY > 0 && screenX > 0) {
            if (screenX < EDGE_PX) {
                if (screenY > camera.viewportHeight - EDGE_PX) {
                    camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
                            camera.position.y - DELTA_PX, 0), 0.1f);
                } else if (screenY < EDGE_PX) {
                    camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
                            camera.position.y + DELTA_PX, 0), 0.1f);
                } else {
                    camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
                            camera.position.y, 0), 0.1f);
                }
            } else if (screenX > camera.viewportWidth - EDGE_PX) {
                if (screenY < EDGE_PX) {
                    camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
                            camera.position.y + DELTA_PX, 0), 0.1f);
                } else if (screenY > camera.viewportHeight - EDGE_PX) {
                    camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
                            camera.position.y - DELTA_PX, 0), 0.1f);
                } else {
                    camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
                            camera.position.y, 0), 0.1f);
                }
            } else if (screenY < EDGE_PX) {
                camera.position.lerp(new Vector3(camera.position.x,
                        camera.position.y + DELTA_PX, 0), 0.1f);
            } else if (screenY > camera.viewportHeight - EDGE_PX) {
                camera.position.lerp(new Vector3(camera.position.x,
                        camera.position.y - DELTA_PX, 0), 0.1f);
            }
        }
    }

}
