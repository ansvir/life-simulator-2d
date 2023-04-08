package com.itique.ls2d.mapeditor.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.constant.world.DefaultGreenCity;
import com.itique.ls2d.custom.actor.CityMapActor;
import com.itique.ls2d.custom.component.SplitComponent;
import com.itique.ls2d.custom.component.style.MapStyle;
import com.itique.ls2d.custom.component.style.ToolbarStyle;
import com.itique.ls2d.custom.listener.ControlListener;
import com.itique.ls2d.custom.listener.AbstractListener;
import com.itique.ls2d.custom.listener.MapNavigationListener;
import com.itique.ls2d.custom.listener.ToolbarListener;
import com.itique.ls2d.mapeditor.actor.MapActor;
import com.itique.ls2d.model.world.Terrain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.Input.Keys.S;

public class EditorScreen implements Screen {

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
    private Stage stage;
    private SplitComponent splitComponent;
    private MapActor mapActor;
    private Table toolbarTable;

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
        stage = new Stage();
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
        splitComponent = new SplitComponent(mapActor, toolBar);
        mainContainer.add(splitComponent).expand().fill();
        mainContainer.setFillParent(true);
        mainContainer.debugAll();
        stage.addActor(mainContainer);
        stage.setViewport(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
//        mapNavigationListener.mouseMoved(Gdx.input.getX(), Gdx.input.getY());
//        processKeyDown();
        stage.act(delta);
        camera.position.x = mainContainer.getX() + mainContainer.getWidth() / 2;
        camera.position.y = mainContainer.getY() + mainContainer.getHeight() / 2;
        camera.update();
        ScreenUtils.clear(Color.WHITE);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        splitComponent.dispose();
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

//    private void processKeyDown() {
//        int keyDown = Gdx.app.getInput().isKeyPressed(A) ? A
//                : Gdx.app.getInput().isKeyPressed(W) ? W
//                : Gdx.app.getInput().isKeyPressed(D) ? D
//                : Gdx.app.getInput().isKeyPressed(S) ? S
//                : -1;
//        if (keyDown != -1) {
//            mapNavigationListener.keyDown(keyDown);
//        }
//    }

    private void createToolbar() {
        Label mapNameLabel = new Label("NAME", skin);
        Label mapWidthLabel = new Label("WIDTH", skin);
        Label mapHeightLabel = new Label("HEIGHT", skin);
        TextField name = new TextField("some map 2", skin);
        TextField width = new TextField(String.valueOf(mapWidth), skin);
        TextField height = new TextField(String.valueOf(mapHeight), skin);
        TextButton exit = new TextButton("EXIT", skin);
        TextButton save = new TextButton("SAVE", skin);
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
        toolBar.setResizable(false);
        toolBar.setModal(false);
        toolBar.setMovable(false);
        toolBar.add(toolbarTable).expand().fill().top();
    }
    
}
