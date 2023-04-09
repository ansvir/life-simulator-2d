package com.itique.ls2d.mapeditor.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.constant.world.DefaultGreenCity;
import com.itique.ls2d.mapeditor.actor.MapActor;
import com.itique.ls2d.model.world.Terrain;

import java.util.Map;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;

public class EditorScreen implements Screen, InputProcessor {

//    private static final int BORDER_DIAPASON = 5;

    private Game game;
    private Skin skin;
    private TextureAtlas atlas;
    private Table mainContainer;
    private Map<Terrain, Pixmap> terrainPixmaps;
    private Texture mapTexture;
    private Pixmap mapPixmap;
    private long mapWidth;
    private long mapHeight;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Window toolBar;
    private Stage mapStage;
    private Stage uiStage;
    private MapActor mapActor;
    private Table toolbarTable;
    private SpriteBatch batch;
    private InputMultiplexer multiplexer;

    public EditorScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        mapWidth = 910L;
        mapHeight = 910L;
        atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        skin = new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();
        mapStage = new Stage(viewport, batch);
        uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        mainContainer = new Table();
        mapPixmap = new Pixmap((int) mapWidth, (int) mapHeight, Pixmap.Format.RGBA8888);
        mapPixmap.setColor(Color.valueOf("178693"));
        mapPixmap.fill();
        mapTexture = new Texture(mapPixmap);
        mapActor = new MapActor(mapTexture, mapWidth, mapHeight, camera);
        terrainPixmaps = new DefaultGreenCity().getTerrainTexturesPaths()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> new Pixmap(Gdx.files.internal(v.getValue()))));
        createToolbar();
        mainContainer.add(toolBar).expand().fill().top().right();
        mapStage.addActor(mapActor);
        uiStage.addActor(mainContainer);
        multiplexer = new InputMultiplexer(this, uiStage, mapStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
//        mapNavigationListener.mouseMoved(Gdx.input.getX(), Gdx.input.getY());
//        processKeyDown();
        ScreenUtils.clear(Color.valueOf("178693"));
//        if (Gdx.input.getX() >= mainContainer.getX()
//                && Gdx.input.getX() <= mainContainer.getX() + mainContainer.getWidth()
//                && Gdx.input.getY() >= mainContainer.getY()
//                && Gdx.input.getY() <= mainContainer.getY() + mainContainer.getHeight()) {
//            Gdx.input.setInputProcessor(uiStage);
//            System.out.println("toolbar");
//        } else if (Gdx.input.getX() <= mainContainer.getX()
//                && Gdx.input.getX() >= mainContainer.getX() + mainContainer.getWidth()
//                && Gdx.input.getY() <= mainContainer.getY()
//                && Gdx.input.getY() >= mainContainer.getY() + mainContainer.getHeight()) {
//            Gdx.input.setInputProcessor(mapStage);
//            System.out.println("MAP ACTOR");
//        }
        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
        int key = getKeyDown();
        if (key != -1) {
            keyDown(key);
        }
        mainContainer.setSize(toolBar.getWidth(), toolBar.getHeight());
        batch.begin();
        uiStage.act(delta);
        mapStage.act(delta);
//        camera.position.x = mainContainer.getX() + mainContainer.getWidth() / 2;
//        camera.position.y = mainContainer.getY() + mainContainer.getHeight() / 2;
        camera.update();
        batch.end();
        mapStage.draw();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mapStage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        terrainPixmaps.values().forEach(Pixmap::dispose);
        mapPixmap.dispose();
        mapTexture.dispose();
        mapStage.dispose();
        uiStage.dispose();
        batch.dispose();
    }

//    private void drawCityBorders() {
//        mapPixmap.setColor(Color.RED);
//        for (int y = 0; y <= mapPixmap.getHeight(); y += BORDER_DIAPASON) {
//            int drawY = y;
//            mapPixmap.drawPixel(0, drawY++);
//            mapPixmap.drawPixel(0, drawY);
//        }
//        for (int y = 0; y <= mapPixmap.getHeight(); y += BORDER_DIAPASON) {
//            int drawY = y;
//            mapPixmap.drawPixel(mapPixmap.getWidth() - 1, drawY++);
//            mapPixmap.drawPixel(mapPixmap.getWidth() - 1, drawY);
//        }
//        for (int x = 0; x <= mapPixmap.getHeight(); x += BORDER_DIAPASON) {
//            int drawX = x;
//            mapPixmap.drawPixel(drawX++, 0);
//            mapPixmap.drawPixel(drawX, 0);
//        }
//        for (int x = 0; x <= mapPixmap.getHeight(); x += BORDER_DIAPASON) {
//            int drawX = x;
//            mapPixmap.drawPixel(drawX++, mapPixmap.getHeight() - 1);
//            mapPixmap.drawPixel(drawX, mapPixmap.getHeight() - 1);
//        }
//    }

    private void createToolbar() {
        Label mapNameLabel = new Label("NAME", skin);
        Label mapWidthLabel = new Label("WIDTH", skin);
        Label mapHeightLabel = new Label("HEIGHT", skin);
        TextField name = new TextField("some map 2", skin);
        addTextChangeListener(name, true);
        TextField width = new TextField(String.valueOf(mapWidth), skin);
        addTextChangeListener(width, false);
        TextField height = new TextField(String.valueOf(mapHeight), skin);
        addTextChangeListener(height, false);
        TextButton exit = new TextButton("EXIT", skin);
        exit.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game));
                return true;
            }
        });
        TextButton save = new TextButton("SAVE", skin);
        save.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mapWidth = Integer.parseInt(width.getText());
                mapHeight = Integer.parseInt(height.getText());
                mapActor.setWidth(mapWidth);
                mapActor.setHeight(mapHeight);
                mapActor.drawMapBorders();
                return true;
            }
        });
        TextButton load = new TextButton("LOAD", skin);
        float pad = 10;
        toolbarTable = new Table();
        toolbarTable.setFillParent(true);
        toolbarTable.pad(30);
        toolbarTable.row().padBottom(pad);
        toolbarTable.add(mapNameLabel).expandX().fill().left();
        toolbarTable.add(name).expandX().fill().colspan(2).center();
        toolbarTable.row().padBottom(pad);
        toolbarTable.add(mapWidthLabel).expandX().fill().left();
        toolbarTable.add(width).expandX().fill().colspan(2);
        toolbarTable.row().padBottom(pad);
        toolbarTable.add(mapHeightLabel).expandX().fill().left();
        toolbarTable.add(height).expandX().fill().colspan(2).center();
        toolbarTable.row();
        toolbarTable.add(save).padLeft(pad)
                .padRight(pad).expandX().fill().left();
        toolbarTable.add(load).padLeft(pad)
                .padRight(pad).expandX().fill().center();
        toolbarTable.add(exit).padLeft(pad)
                .padRight(pad).expandX().fill().right();
        toolBar = new Window("Toolbar", skin);
        toolBar.setResizable(true);
        toolBar.setModal(false);
        toolBar.setMovable(true);
        toolBar.add(toolbarTable).expand().fill();
        toolBar.setSize(toolBar.getPrefWidth(), toolBar.getPrefHeight());
    }

    private int getKeyDown() {
        return Gdx.app.getInput().isKeyPressed(A) ? A
                : Gdx.app.getInput().isKeyPressed(W) ? W
                : Gdx.app.getInput().isKeyPressed(D) ? D
                : Gdx.app.getInput().isKeyPressed(S) ? S
                : -1;
    }

    private boolean isHoverToolbar() {
        System.out.println(Gdx.input.getX() >= mainContainer.getX()
                && Gdx.input.getX() <= mainContainer.getX() + mainContainer.getWidth()
                && Gdx.input.getY() >= mainContainer.getY()
                && Gdx.input.getY() <= mainContainer.getY() - mainContainer.getHeight());
        return Gdx.input.getX() >= mainContainer.getX()
                && Gdx.input.getX() <= mainContainer.getX() + mainContainer.getWidth()
                && Gdx.input.getY() >= mainContainer.getY()
                && Gdx.input.getY() <= mainContainer.getY() - mainContainer.getHeight();
    }

    @Override
    public boolean keyDown(int keycode) {
        int key = getKeyDown();
        if (key != -1 && !isHoverToolbar()) {
            InputEvent keyTyped = new InputEvent();
            keyTyped.setType(InputEvent.Type.keyDown);
            keyTyped.setStage(mapStage);
            keyTyped.setKeyCode(key);
            mapActor.fire(keyTyped);
        }
        if (isHoverToolbar()) {
            InputEvent keyTyped = new InputEvent();
            keyTyped.setType(InputEvent.Type.keyDown);
            keyTyped.setStage(uiStage);
            keyTyped.setKeyCode(key);
            mainContainer.fire(keyTyped);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        int key = getKeyDown();
        if (key != -1 && !isHoverToolbar()) {
            InputEvent keyUp = new InputEvent();
            keyUp.setType(InputEvent.Type.keyUp);
            keyUp.setStage(mapStage);
            keyUp.setKeyCode(key);
            mapActor.fire(keyUp);
        }
        if (isHoverToolbar()) {
            InputEvent keyUp = new InputEvent();
            keyUp.setType(InputEvent.Type.keyUp);
            keyUp.setStage(uiStage);
            keyUp.setKeyCode(keycode);
            mainContainer.fire(keyUp);
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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!isHoverToolbar()) {
            InputEvent mouseMoved = new InputEvent();
            mouseMoved.setType(InputEvent.Type.mouseMoved);
            mouseMoved.setStage(mapStage);
            mouseMoved.setStageX(screenX);
            mouseMoved.setStageY(screenY);
            mapActor.fire(mouseMoved);
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (!isHoverToolbar()) {
            InputEvent scrolled = new InputEvent();
            scrolled.setType(InputEvent.Type.scrolled);
            scrolled.setStage(mapStage);
            scrolled.setStageX(Gdx.input.getX());
            scrolled.setStageY(Gdx.input.getY());
            scrolled.setScrollAmountY(amountY);
            mapActor.fire(scrolled);
        }
        return true;
    }

    private void addTextChangeListener(TextField field, boolean text) {
        field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isHoverToolbar()) {
                    event.cancel();
                }
                if (!text) {
                    try {
                        Long.parseLong(field.getText());
                    } catch (NumberFormatException e) {
                        event.cancel();
                    }
                }
            }
        });
    }
}
