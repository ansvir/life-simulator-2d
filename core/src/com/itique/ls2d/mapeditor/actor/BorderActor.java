package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class BorderActor extends AbstractGroup implements Disposable {

    private static final int BORDER_DIAPASON = 2;

    private OrthographicCamera camera;
    private float width;
    private float height;
    private ShapeRenderer renderer;

    public BorderActor(OrthographicCamera camera, float width, float height) {
        super(new Vector2(0, 0), new Vector2(width, height));
        this.camera = camera;
        this.width = width;
        this.height = height;
        renderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        drawBorders(batch);
        batch.begin();
    }

    private void drawBorders(Batch batch) {
        int dotSize = (int) (camera.zoom * 3f);
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setColor(Color.RED);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int y = 0; y <= height; y += BORDER_DIAPASON * dotSize) {
            renderer.rect(0, y, dotSize, dotSize);
        }
        for (int y = 0; y <= height; y += BORDER_DIAPASON * dotSize) {
            renderer.rect(height - dotSize, y, dotSize, dotSize);
        }
        for (int x = 0; x <= width; x += BORDER_DIAPASON * dotSize) {
            renderer.rect(x, 0, dotSize, dotSize);
        }
        for (int x = 0; x <= width; x += BORDER_DIAPASON * dotSize) {
            renderer.rect(x, height - dotSize, dotSize, dotSize);
        }
        renderer.end();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

}
