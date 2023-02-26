package com.itique.ps.custom;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LerpCamera {

    private static final float DELTA = 10f;
    private Vector3 start;
    private Vector3 back;
    private OrthographicCamera camera;

    public LerpCamera(OrthographicCamera camera) {
        this.start = new Vector3(camera.viewportWidth / 2f,
                camera.viewportHeight / 2f, 0);
        this.back = new Vector3(camera.viewportWidth / 2f,
                camera.viewportHeight / 2f, 0);
        this.camera = camera;
    }

    public void moveBack(float x, float y) {
        this.camera.position.lerp(new Vector3(x, y, 0), 0.1f);
    }

    public void moveForward() {
        this.camera.position.lerp(new Vector3(this.camera.viewportWidth / 2f,
                this.camera.viewportHeight / 2f, 0), 0.1f);
    }

}
