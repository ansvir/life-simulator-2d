package com.itique.ls2d.custom.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplitComponent extends Actor implements InputProcessor, Disposable {

    private static final float WIDTH = 6f;
    private Actor first;
    private Actor second;
    private InputProcessor firstInputProcessor;
    private InputProcessor secondInputProcessor;
    private Stage stage;
    private ShapeRenderer renderer;
    private float xPos;
    private float paneStart;
    private float paneEnd;
    private boolean isDragging;
    private Pixmap cursorPixmap;
    private Cursor cursor;

    public SplitComponent(Actor first, Actor second, InputProcessor firstInputProcessor, InputProcessor secondInputProcessor, Stage stage) {
        this.first = first;
        this.second = second;
        this.firstInputProcessor = firstInputProcessor;
        this.secondInputProcessor = secondInputProcessor;
        this.stage = stage;
        stage.addActor(first);
        stage.addActor(second);
        renderer = new ShapeRenderer();
        xPos = Gdx.graphics.getWidth() / 2f;
        paneStart = xPos - WIDTH / 2f;
        paneEnd = xPos + WIDTH / 2f;
        isDragging = false;
        cursorPixmap = new Pixmap(Gdx.files.internal("textures/cursor.png"));
        cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(xPos, 0f, WIDTH, Gdx.graphics.getHeight());
        renderer.end();
        batch.begin();
        float pointerX = Gdx.input.getX();
        if (pointerX < paneStart) {
            Gdx.input.setInputProcessor(firstInputProcessor);
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
//            stage.getViewport().setScreenBounds( 0, 0, (int) paneStart - 1, Gdx.graphics.getHeight());
//            batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        } else if (pointerX > paneEnd) {
            Gdx.input.setInputProcessor(secondInputProcessor);
            Gdx.graphics.setCursor(cursor);
//            stage.getViewport().setScreenBounds((int) paneEnd + 1, 0, Gdx.graphics.getWidth() - (int) paneEnd ,Gdx.graphics.getHeight());
//            batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        } else {
            Gdx.input.setInputProcessor(this);
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.HorizontalResize);
        }
        second.setX(paneEnd + 1);
//        Matrix4 projection = stage.getCamera().projection;
//        float screenWidth = stage.getViewport().getScreenWidth();
//        float screenHeight = stage.getViewport().getScreenHeight();
//        second.setWidth(projection.getScaleX() * screenWidth * 2 - paneEnd - 1);
//        second.setHeight(projection.getScaleY() * screenHeight * 2);
        second.setWidth(stage.getViewport().getScreenWidth() - paneEnd - 1);
        second.setHeight(stage.getViewport().getScreenHeight());
    }

    public boolean isHoverPane(float x) {
        return x >= paneStart && x <= paneEnd;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isHoverPane(screenX)) {
            isDragging = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragging) {
            isDragging = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging) {
            xPos = Gdx.input.getX();
            paneStart = xPos - WIDTH / 2f;
            paneEnd = xPos + WIDTH / 2f;
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void dispose() {
        cursorPixmap.dispose();
        cursor.dispose();
        renderer.dispose();
    }

}
