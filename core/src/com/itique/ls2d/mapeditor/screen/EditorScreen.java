package com.itique.ls2d.mapeditor.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.constant.world.DefaultGreenCity;
import com.itique.ls2d.mapeditor.actor.BorderActor;
import com.itique.ls2d.mapeditor.actor.MapActor;
import com.itique.ls2d.mapeditor.actor.MapGroup;
import com.itique.ls2d.model.world.Terrain;
import com.itique.ls2d.util.ResourceUtil;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;

public class EditorScreen implements Screen, InputProcessor {

    private static final int BUFFER_SIZE = 10;

    private Game game;
    private Skin skin;
    private TextureAtlas atlas;
    private Table mainContainer;
    private Map<Terrain, Pixmap> terrainPixmaps;
    private Terrain currentTexture;
    private Pixmap mapPixmap;
    private long mapWidth;
    private long mapHeight;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Window toolBar;
    private Stage mapStage;
    private Stage uiStage;
    private MapGroup mapGroup;
    private Table toolbarTable;
    private SpriteBatch batch;
    private InputMultiplexer multiplexer;
    private boolean isOnUiFocus;
    private boolean isDrawing;
    private Cursor cursor;
    private Pixmap cursorPixmap;
    private ConcurrentLinkedQueue<Pixmap> pixmapBuffer;
    private boolean controlIsDown;
    private Pixmap brush;
    private float brushSize;
    private boolean isBrush;
    private Window topPane;
    private Label infoLabel;
    private BorderActor borders;
    private MapActor mapActor;

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
        mapGroup = new MapGroup(camera, mapWidth, mapHeight);
        mapGroup.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isOnUiFocus = false;
            }
        });
        borders = new BorderActor(camera, mapWidth, mapHeight);
        mapActor = new MapActor(mapPixmap, new Vector2(mapWidth, mapHeight));
        mapGroup.addObject(mapActor);
        mapGroup.addObject(borders);
        borders.toFront();
        terrainPixmaps = new DefaultGreenCity().getTerrainTexturesPaths()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> new Pixmap(Gdx.files.internal(v.getValue()))));
        currentTexture = Terrain.SAND;
        createToolbar();
        createTopPane();
        mainContainer.add(topPane).expand().fill().height(Gdx.graphics.getHeight() / 8f).top().colspan(2);
        mainContainer.row().height(Gdx.graphics.getHeight() - topPane.getHeight());
        infoLabel = new Label(currentTexture.name().toLowerCase() + "\n" + Math.round(camera.zoom * 100) + " %", skin);
        mainContainer.add(infoLabel).pad(10).grow().bottom();
        mainContainer.add(toolBar).width(Gdx.graphics.getWidth() / 3.5f).right().top();

        toolBar.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isOnUiFocus = true;
            }
        });
        mainContainer.setFillParent(true);
        mapStage.addActor(mapGroup);
        uiStage.addActor(mainContainer);
        cursorPixmap = new Pixmap(Gdx.files.internal("textures/cursor.png"));
        cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
        multiplexer = new InputMultiplexer(this, mapStage, uiStage);
        Gdx.input.setInputProcessor(multiplexer);
        pixmapBuffer = new ConcurrentLinkedQueue<>();
        pixmapBuffer.add(mapPixmap);
        brushSize = 50f;
        currentTexture = Terrain.SAND;
        mainContainer.debugAll();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.valueOf("178693"));
        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
        keyDown(getKeyDown());
        batch.begin();
        uiStage.act(delta);
        mapStage.act(delta);
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
        mapStage.dispose();
        uiStage.dispose();
        batch.dispose();
        cursor.dispose();
        cursorPixmap.dispose();
        brush.dispose();
    }

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
        TextButton save = new TextButton("SAVE", skin);
        save.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mapWidth = Integer.parseInt(width.getText());
                mapHeight = Integer.parseInt(height.getText());
                mapGroup.setWidth(mapWidth);
                mapGroup.setHeight(mapHeight);
                return true;
            }
        });
        List<ImageButton> terrainTexturesButtons = terrainPixmaps.entrySet().stream()
                .filter(e -> e.getKey() == Terrain.SAND || e.getKey() == Terrain.GRASS
                        || e.getKey() == Terrain.WATER || e.getKey() == Terrain.ROCK
                        || e.getKey() == Terrain.TREE)
                .map(e -> {
                    Pixmap newPixmap = ResourceUtil.resizePixmap(e.getValue(), 50, 50);
                    Texture newTexture = new Texture(newPixmap);
                    ImageButton terrain = new ImageButton(new SpriteDrawable(new Sprite(newTexture)));
                    terrain.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            currentTexture = e.getKey();
                            infoLabel.setText(currentTexture.name().toLowerCase() + "\n" + Math.round(camera.zoom * 100) + " %");
                            brush = terrainPixmaps.get(currentTexture);
                            updateBrush(true);
                        }
                    });
                    return terrain;
                }).collect(Collectors.toList());
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
        Window palette = new Window("Palette", skin);
        palette.setMovable(false);
        palette.setModal(false);
        palette.setResizable(false);
        for (int i = 0; i < terrainTexturesButtons.size();) {
            for (int j = 0; j < 3 && i < terrainTexturesButtons.size(); j++, i++) {
                palette.add(terrainTexturesButtons.get(i)).pad(pad);
            }
            palette.row();
        }
        toolbarTable.row().padBottom(pad);
        toolbarTable.add(palette).expandX().fill().colspan(3);
        toolbarTable.row().padBottom(pad);
        Slider slider = new Slider(1f, 2000f, 20f, false, skin);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                brushSize = slider.getValue();
            }
        });
        slider.setValue(brushSize);
        toolbarTable.add(slider).expandX().fill().colspan(3);
        toolbarTable.row().padBottom(pad);
        toolbarTable.add(save).padLeft(pad)
                .padRight(pad).expandX().fill().colspan(3);
        toolBar = new Window("Toolbar", skin);
        toolBar.setResizable(true);
        toolBar.setModal(true);
        toolBar.setMovable(true);
        toolBar.add(toolbarTable).expand().fill();
        toolBar.addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                toolBar.setPosition(MathUtils.clamp(toolBar.getX(),
                                0, Gdx.graphics.getWidth() - toolBar.getWidth()),
                        MathUtils.clamp(toolBar.getY(), 0,
                                Gdx.graphics.getHeight() - toolBar.getHeight()));
            }
        });
    }

    private void createTopPane() {
        TextButton load = new TextButton("Load Map", skin);
        TextButton loadImage = new TextButton("Load Image", skin);
        TextButton toggleToolbar = new TextButton("Toggle toolbar", skin);
        toggleToolbar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toolBar.setVisible(!toolBar.isVisible());
            }
        });

        TextButton exit = new TextButton("Exit", skin);
        exit.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game));
                return true;
            }
        });
        topPane = new Window("Map Editor v1.0", skin);
        topPane.setMovable(false);
        topPane.setResizable(false);
        topPane.setModal(false);
        topPane.align(Align.left);
        topPane.add(load).pad(10);
        topPane.add(loadImage).pad(10);
        topPane.add(toggleToolbar).pad(10);
        topPane.add(exit).pad(10);
        topPane.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isOnUiFocus = true;
            }

        });
    }

    private int getKeyDown() {
        return Gdx.app.getInput().isKeyPressed(A) ? A
                : Gdx.app.getInput().isKeyPressed(W) ? W
                : Gdx.app.getInput().isKeyPressed(D) ? D
                : Gdx.app.getInput().isKeyPressed(S) ? S
                : -1;
    }

    private boolean isMapMoveKey(int keycode) {
        return keycode == A || keycode == W || keycode == D || keycode == S;
    }

    private void addTextChangeListener(TextField field, boolean text) {
        field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isOnUiFocus) {
                    event.cancel();
                }
                if (!text) {
                    try {
                        if (!field.getText().isBlank()) {
                            Long.parseLong(field.getText());
                        }
                    } catch (NumberFormatException e) {
                        event.cancel();
                    }
                }
            }
        });
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!isOnUiFocus) {
            if (isMapMoveKey(keycode)) {
                InputEvent keyTyped = new InputEvent();
                keyTyped.setType(InputEvent.Type.keyDown);
                keyTyped.setStage(mapStage);
                keyTyped.setKeyCode(keycode);
                return mapGroup.fire(keyTyped);
            }
            if (keycode == CONTROL_LEFT || keycode == CONTROL_RIGHT) {
                controlIsDown = true;
            }
            if (controlIsDown && keycode == Z) {
                updateBuffer(true);
                return true;
            }
        }
        if (keycode == ESCAPE && isBrush) {
            updateBrush(false);
            return true;
        }
        if (keycode == J) {
            System.out.println("isBrush = " + isBrush + ", isDrawing = " + isDrawing + ", isOnUiFocus = " + isOnUiFocus + ", pixmapBuffer.size() = " + pixmapBuffer.size());
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!isOnUiFocus) {
            InputEvent keyUp = new InputEvent();
            keyUp.setType(InputEvent.Type.keyUp);
            keyUp.setStage(mapStage);
            keyUp.setKeyCode(keycode);
            mapGroup.fire(keyUp);
            if (keycode == CONTROL_LEFT || keycode == CONTROL_RIGHT) {
                controlIsDown = false;
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isBrush) {
            isDrawing = true;
            mapPixmap.drawPixmap(brush, screenX, screenY, 0, 0, (int) brushSize, (int) brushSize);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isBrush) {
            isDrawing = false;
            updateBuffer(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDrawing) {
            mapPixmap.drawPixmap(brush, screenX, screenY, 0, 0, (int) brushSize, (int) brushSize);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!isOnUiFocus) {
            InputEvent mouseMoved = new InputEvent();
            mouseMoved.setType(InputEvent.Type.mouseMoved);
            mouseMoved.setStage(mapStage);
            mouseMoved.setStageX(screenX);
            mouseMoved.setStageY(screenY);
            mapGroup.fire(mouseMoved);
            return true;
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (!isOnUiFocus) {
            InputEvent scrolled = new InputEvent();
            scrolled.setType(InputEvent.Type.scrolled);
            scrolled.setStage(mapStage);
            scrolled.setStageX(Gdx.input.getX());
            scrolled.setStageY(Gdx.input.getY());
            scrolled.setScrollAmountY(amountY);
            mapGroup.fire(scrolled);
            infoLabel.setText(currentTexture.name().toLowerCase() + "\n" + Math.round(camera.zoom * 100) + " %");
            return true;
        }
        return false;
    }

    private void updateBuffer(boolean undo) {
        if (undo) {
            mapPixmap.dispose();
            try {
                mapPixmap = new Pixmap(pixmapBuffer.remove().getPixels());
            } catch (NoSuchElementException ignored) {
            }
        } else {
            if (pixmapBuffer.size() <= BUFFER_SIZE) {
                pixmapBuffer.add(mapPixmap);
            } else {
                ConcurrentLinkedQueue<Pixmap> temp = new ConcurrentLinkedQueue<>();
                int size = pixmapBuffer.size();
                for (int i = 1; i < size; i++) {
                    temp.add(pixmapBuffer.poll());
                }
                pixmapBuffer = new ConcurrentLinkedQueue<>();
                pixmapBuffer.add(mapPixmap);
                pixmapBuffer.addAll(temp);
            }
        }
    }

    private void updateBrush(boolean brushOn) {
        if (brushOn) {
            isBrush = true;
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        } else {
            isBrush = false;
            Gdx.graphics.setCursor(cursor);
        }
    }
}
