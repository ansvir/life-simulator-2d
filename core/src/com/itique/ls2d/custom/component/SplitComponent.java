package com.itique.ls2d.custom.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.utils.Disposable;
import com.itique.ls2d.util.ResourceUtil;

public class SplitComponent extends SplitPane implements Disposable {

    private static final float WIDTH = 4f;
    private Actor first;
    private Actor second;
    private float lastXPos;
    private float startXPos;
    private float paneStart;
    private float paneEnd;
    private boolean isDragging;
    private Pixmap cursorPixmap;
    private Cursor cursor;
    private ShapeRenderer renderer;

    public SplitComponent(Actor first, Actor second) {
        super(first, second, false, ResourceUtil.getDefaultSkin());
        this.first = first;
        this.second = second;
        this.renderer = new ShapeRenderer();
        this.setSplitAmount(0.5f);
        lastXPos = this.getWidth() / 2;
        paneStart = lastXPos - WIDTH / 2f;
        paneEnd = lastXPos + WIDTH / 2f;
        isDragging = false;
        cursorPixmap = new Pixmap(Gdx.files.internal("textures/cursor.png"));
        cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isHoverPane(x)) {
                    isDragging = true;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isDragging) {
                    isDragging = false;
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isDragging) {
                    paneStart = x - WIDTH / 2f;
                    paneEnd = x + WIDTH / 2f;
                    calculateSplit((int) x);
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(lastXPos, 0f, WIDTH, Math.max(first.getHeight(), second.getHeight()));
        renderer.end();
        batch.begin();
        float pointerX = Gdx.input.getX();
        if (pointerX < paneStart) {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        } else if (pointerX > paneEnd) {
            Gdx.graphics.setCursor(cursor);
        } else {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.HorizontalResize);
        }
        second.setX(paneEnd + 1);
        first.draw(batch, parentAlpha);
        second.draw(batch, parentAlpha);
    }

    public boolean isHoverPane(float x) {
        return x >= paneStart && x <= paneEnd;
    }

//    @Override
//    public boolean keyDown(int keycode) {
//        return false;
//    }
//
//    @Override
//    public boolean keyUp(int keycode) {
//        return false;
//    }
//
//    @Override
//    public boolean keyTyped(char character) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        if (isHoverPane(screenX)) {
//            isDragging = true;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        if (isDragging) {
//            isDragging = false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        if (isDragging) {
//            calculateSplit(screenX);
//            xPos = Gdx.input.getX() < xPos ? Gdx.input.getX() - 1
//                    : Gdx.input.getX() > xPos ? Gdx.input.getX() + 1 : xPos;
//            paneStart = xPos - WIDTH / 2f;
//            paneEnd = xPos + WIDTH / 2f;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean mouseMoved(int screenX, int screenY) {
//        return false;
//    }
//
//    @Override
//    public boolean scrolled(float amountX, float amountY) {
//        return false;
//    }

    @Override
    public void dispose() {
        cursorPixmap.dispose();
        cursor.dispose();
        renderer.dispose();
    }

    private void calculateSplit(int screenX) {
        startXPos = this.getSplitAmount();
        float delta = lastXPos < screenX ? -1 : 1;
        float splitAmount = MathUtils.clamp(startXPos, 0, 1);
        this.setSplitAmount(splitAmount + delta / (this.getWidth()));
        second.setWidth((splitAmount <= 0.5 ? 1 - splitAmount : splitAmount) * (this.getWidth() - paneStart - 1));
        lastXPos = screenX;
    }

}
