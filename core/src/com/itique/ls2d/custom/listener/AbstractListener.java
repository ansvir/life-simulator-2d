package com.itique.ls2d.custom.listener;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.itique.ls2d.custom.component.style.Style;

import java.util.Arrays;

public abstract class AbstractListener implements InputProcessor {

    private Stage stage;
    private Style style;

    public AbstractListener(Stage stage, Style style) {
        this.stage = stage;
        this.style = style;
    }

    @Override
    public abstract boolean keyDown(int keycode);

    @Override
    public abstract boolean keyUp(int keycode);

    @Override
    public abstract boolean keyTyped(char character);

    @Override
    public abstract boolean touchDown(int screenX, int screenY, int pointer, int button);

    @Override
    public abstract boolean touchUp(int screenX, int screenY, int pointer, int button);

    @Override
    public abstract boolean touchDragged(int screenX, int screenY, int pointer);

    @Override
    public abstract boolean mouseMoved(int screenX, int screenY);

    @Override
    public abstract boolean scrolled(float amountX, float amountY);

    public Stage getStage() {
        return stage;
    }

    public Style getStyle() {
        return style;
    }
}
