package com.itique.ls2d.mapeditor.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.constant.RGBAConstant;
import com.itique.ls2d.constant.world.DefaultGreenCity;
import com.itique.ls2d.mapeditor.actor.BorderActor;
import com.itique.ls2d.mapeditor.actor.MapActor;
import com.itique.ls2d.mapeditor.actor.MapGroup;
import com.itique.ls2d.mapeditor.actor.RoadActor;
import com.itique.ls2d.mapeditor.model.MapModel;
import com.itique.ls2d.mapeditor.model.RoadModel;
import com.itique.ls2d.model.world.Terrain;
import com.itique.ls2d.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.itique.ls2d.constant.Constant.ROAD_HEIGHT;
import static com.itique.ls2d.constant.Constant.ROAD_WIDTH;
import static com.itique.ls2d.mapeditor.model.RoadModel.Type.*;
import static java.lang.Math.abs;

public class EditorScreen implements Screen, InputProcessor {

    private static final int BUFFER_SIZE = 10;

    private Game game;
    private Skin skin;
    private TextureAtlas atlas;
    private Group uiGroup;
    private Map<Terrain, Pixmap> terrainPixmaps;
    private Terrain currentTerrain;
    private Pixmap mapPixmap;
    private Texture staticTexture;
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
    private Window properties;
    private RoadModel.Builder roadBuilder;
    private ShapeRenderer renderer;
    private MapModel mapModel;
    private RoadActor currentRoad;
    private float xEnd;
    private float yEnd;
    private Stage currentStage;

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
        renderer = new ShapeRenderer();
        mapStage = new Stage(viewport, batch);
        uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        uiGroup = new Group();
        mapPixmap = new Pixmap((int) mapWidth, (int) mapHeight, Pixmap.Format.RGBA8888);
        mapPixmap.setColor(Color.valueOf("178693"));
        mapPixmap.fill();
        mapModel = new MapModel("some map 2", mapWidth, mapHeight, new ArrayList<>(), new ArrayList<>(),
                Base64Coder.encodeLines(getBytesPixels()));
        mapGroup = new MapGroup(camera, mapWidth, mapHeight);
        borders = new BorderActor(camera, mapWidth, mapHeight);
        mapPixmap = new Pixmap((int) mapWidth, (int) mapHeight, Pixmap.Format.RGBA8888);
        staticTexture = new Texture(mapPixmap);
        mapActor = new MapActor(staticTexture, new Vector2(mapWidth, mapHeight));
        mapGroup.addObject(mapActor);
        mapGroup.addObject(borders);
        borders.toFront();
        terrainPixmaps = new DefaultGreenCity().getTerrainTexturesPaths()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> new Pixmap(Gdx.files.internal(v.getValue()))));
        currentTerrain = Terrain.ROAD;
        createTopPane();
        createToolbar();
        uiGroup.addActor(topPane);
        infoLabel = new Label(currentTerrain.name().toLowerCase() + "\n" + Math.round(camera.zoom * 100) + " %", skin);
        infoLabel.setSize(Gdx.graphics.getWidth() / 18f, Gdx.graphics.getHeight() / 7f);
        infoLabel.setPosition(10, 10);
        uiGroup.addActor(infoLabel);
        uiGroup.addActor(toolBar);
        mapStage.addActor(mapGroup);
        uiStage.addActor(uiGroup);
        cursorPixmap = new Pixmap(Gdx.files.internal("textures/cursor.png"));
        cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
        multiplexer = new InputMultiplexer(this, uiStage, mapStage);
        Gdx.input.setInputProcessor(multiplexer);
        pixmapBuffer = new ConcurrentLinkedQueue<>();
        pixmapBuffer.add(mapPixmap);
        brushSize = 50f;
        uiStage.setDebugAll(true);
        currentStage = mapStage;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.valueOf("178693"));
        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
        keyDown(getKeyDown());
        if (currentStage == uiStage) {
            mapStage.draw();
            uiStage.draw();
            mapStage.act(delta);
            uiStage.act(delta);
        } else {
            uiStage.draw();
            mapStage.draw();
            uiStage.act(delta);
            mapStage.act(delta);
        }
        batch.begin();
        camera.update();
        batch.end();
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
        staticTexture.dispose();
        mapStage.dispose();
        uiStage.dispose();
        batch.dispose();
        cursor.dispose();
        cursorPixmap.dispose();
        brush.dispose();
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
        topPane.setModal(true);
        topPane.align(Align.left);
        topPane.add(load).pad(10);
        topPane.add(loadImage).pad(10);
        topPane.add(toggleToolbar).pad(10);
        topPane.add(exit).pad(10);
        topPane.setHeight(Gdx.graphics.getHeight() / 8f);
        topPane.setWidth(Gdx.graphics.getWidth());
        topPane.setPosition(0, Gdx.graphics.getHeight() - topPane.getHeight());
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
//                updateBuffer(true);
                return true;
            }
        }
        if (keycode == ESCAPE && isBrush) {
            updateBrush(false);
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
        isOnUiFocus = mapStage.hit(screenX, screenY, true) == null;
        if (!isOnUiFocus && isBrush) {
            isDrawing = true;
            if (currentTerrain == Terrain.ROAD) {
                renderer.setColor(new Color(RGBAConstant.ROAD));
                roadBuilder.x(screenX);
                roadBuilder.y(screenY);
            }
//            mapPixmap.drawPixmap(brush, screenX, screenY, 0, 0, (int) brushSize, (int) brushSize);
            return true;
        } else {
            InputEvent touchDown = new InputEvent();
            touchDown.setType(InputEvent.Type.touchDown);
            touchDown.setStage(uiStage);
            touchDown.setStageX(screenX);
            touchDown.setStageY(screenY);
            if (topPane.hit(screenX, screenY, true) == null && toolBar.hit(screenX, screenY, true) != null) {
                toolBar.toFront();
                topPane.toBack();
                toolBar.fire(touchDown);
            } else if (topPane.hit(screenX, screenY, true) != null && toolBar.hit(screenX, screenY, true) == null) {
                topPane.toFront();
                toolBar.toBack();
                topPane.fire(touchDown);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isBrush && isDrawing) {
            isDrawing = false;
            if (currentTerrain == Terrain.ROAD) {
                roadBuilder.width(abs(roadBuilder.getX() - xEnd));
                roadBuilder.height(abs(roadBuilder.getY() - yEnd));
                currentRoad = new RoadActor(renderer, roadBuilder.build());
                mapGroup.addObject(currentRoad);
                mapModel.getRoads().add(currentRoad.getRoadModel());
                roadBuilder = RoadModel.builder();
            }
//            updateBuffer(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isBrush && isDrawing) {
            if (currentTerrain == Terrain.ROAD) {
                if (roadBuilder.getType() == STRAIGHT) {
                    float diffX = screenX - roadBuilder.getX();
                    float diffY = screenY - roadBuilder.getY();
                    if (abs(diffX) > abs(diffY) && diffX < 0) {
                        xEnd = screenX - ROAD_HEIGHT;
                        yEnd = screenY + ROAD_WIDTH;
                    } else if (abs(diffX) > abs(diffY) && diffX > 0) {
                        xEnd = screenX + ROAD_HEIGHT;
                        yEnd = screenY + ROAD_WIDTH;
                    } else if (abs(diffX) < abs(diffY) && diffY < 0) {
                        xEnd = screenX + ROAD_WIDTH;
                        yEnd = screenY - ROAD_HEIGHT;
                    } else if (abs(diffX) > abs(diffY) && diffY > 0) {
                        xEnd = screenX + ROAD_WIDTH;
                        yEnd = screenY + ROAD_HEIGHT;
                    }
                }
            }
//            mapPixmap.drawPixmap(brush, screenX, screenY, 0, 0, (int) brushSize, (int) brushSize);
            return true;
        } else {
            InputEvent touchDragged = new InputEvent();
            touchDragged.setType(InputEvent.Type.touchDragged);
            touchDragged.setStage(uiStage);
            touchDragged.setStageX(screenX);
            touchDragged.setStageY(screenY);
            if (topPane.hit(screenX, screenY, true) == null && toolBar.hit(screenX, screenY, true) != null) {
                toolBar.toFront();
                topPane.toBack();
                toolBar.fire(touchDragged);
            } else if (topPane.hit(screenX, screenY, true) != null && toolBar.hit(screenX, screenY, true) == null) {
                topPane.toFront();
                toolBar.toBack();
                topPane.fire(touchDragged);
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        isOnUiFocus = mapStage.hit(screenX, screenY, true) == null;
        if (!isOnUiFocus) {
            InputEvent mouseMoved = new InputEvent();
            mouseMoved.setType(InputEvent.Type.mouseMoved);
            mouseMoved.setStage(mapStage);
            mouseMoved.setStageX(screenX);
            mouseMoved.setStageY(screenY);
            mapGroup.fire(mouseMoved);
        } else {
            InputEvent mouseMoved = new InputEvent();
            mouseMoved.setType(InputEvent.Type.mouseMoved);
            mouseMoved.setStage(uiStage);
            mouseMoved.setStageX(screenX);
            mouseMoved.setStageY(screenY);
            if (topPane.hit(screenX, screenY, true) == null && toolBar.hit(screenX, screenY, true) != null) {
                toolBar.toFront();
                topPane.toBack();
                toolBar.fire(mouseMoved);
            } else if (topPane.hit(screenX, screenY, true) != null && toolBar.hit(screenX, screenY, true) == null) {
                topPane.toFront();
                toolBar.toBack();
                topPane.fire(mouseMoved);
            }
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
            infoLabel.setText(currentTerrain.name().toLowerCase() + "\n" + Math.round(camera.zoom * 100) + " %");
            return true;
        }
        return false;
    }

//    private void updateBuffer(boolean undo) {
//        if (undo) {
//            try {
//                mapPixmap = new Pixmap(pixmapBuffer.remove().getPixels());
//            } catch (NoSuchElementException ignored) {
//            }
//        } else {
//            if (pixmapBuffer.size() <= BUFFER_SIZE) {
//                pixmapBuffer.add(mapPixmap);
//            } else {
//                ConcurrentLinkedQueue<Pixmap> temp = new ConcurrentLinkedQueue<>();
//                int size = pixmapBuffer.size();
//                for (int i = 1; i < size; i++) {
//                    temp.add(pixmapBuffer.poll());
//                }
//                pixmapBuffer = new ConcurrentLinkedQueue<>();
//                pixmapBuffer.add(mapPixmap);
//                pixmapBuffer.addAll(temp);
//            }
//        }
//    }

    private void updateBrush(boolean brushOn) {
        if (brushOn) {
            isBrush = true;
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        } else {
            isBrush = false;
            Gdx.graphics.setCursor(cursor);
        }
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
                mapModel.setWidth(mapWidth);
                mapModel.setHeight(mapHeight);
                mapModel.setName(name.getText());
                staticTexture = new Texture(mapPixmap);
                mapActor.setStaticTexture(staticTexture);
                return true;
            }
        });
        List<ImageButton> terrainTexturesButtons = terrainPixmaps.entrySet().stream()
                .filter(e -> e.getKey() == Terrain.ROAD || e.getKey() == Terrain.BUILDING)
                .map(e -> {
                    Pixmap newPixmap = ResourceUtil.resizePixmap(e.getValue(), 50, 50);
                    Texture newTexture = new Texture(newPixmap);
                    ImageButton terrain = new ImageButton(new SpriteDrawable(new Sprite(newTexture)));
                    if (e.getKey() == Terrain.ROAD) {
                        terrain.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                properties.clearChildren();
                                roadBuilder = RoadModel.builder();
                                roadBuilder.type(STRAIGHT);
                                TextButton type = new TextButton(STRAIGHT.name().toLowerCase(), skin);
                                type.addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        if (type.getText().toString().equalsIgnoreCase(STRAIGHT.name())) {
                                            type.setText(DIAGONAL.name().toLowerCase());
                                            roadBuilder.type(DIAGONAL);
                                        } else if (type.getText().toString().equalsIgnoreCase(DIAGONAL.name())) {
                                            type.setText(PART.name().toLowerCase());
                                            roadBuilder.type(PART);
                                        } else {
                                            type.setText(STRAIGHT.name().toLowerCase());
                                            roadBuilder.type(STRAIGHT);
                                        }
                                    }
                                });
                                properties.setVisible(true);
                                properties.add(type).pad(10f);
                                currentTerrain = Terrain.ROAD;
                                infoLabel.setText(currentTerrain.name().toLowerCase() + "\n" + Math.round(camera.zoom * 100) + " %");
                                updateBrush(true);
                            }
                        });
                    } else if (e.getKey() == Terrain.BUILDING) {
                        terrain.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                properties.clearChildren();
                                currentTerrain = Terrain.BUILDING;
                                infoLabel.setText(currentTerrain.name().toLowerCase() + "\n" + Math.round(camera.zoom * 100) + " %");
                                updateBrush(true);
                            }
                        });
                    }
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
        properties = new Window("Properties", skin);
        properties.setVisible(false);
        properties.setMovable(false);
        properties.setModal(false);
        properties.setResizable(false);
        toolbarTable.add(properties);
//        Slider slider = new Slider(1f, 2000f, 20f, false, skin);
//        slider.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                brushSize = slider.getValue();
//            }
//        });
//        slider.setValue(brushSize);
//        toolbarTable.add(slider).expandX().fill().colspan(3);
//        toolbarTable.row().padBottom(pad);
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
        toolBar.setSize(Gdx.graphics.getWidth() / 3.5f, Gdx.graphics.getHeight() - topPane.getHeight());
        toolBar.setPosition(Gdx.graphics.getWidth() - toolBar.getWidth(), 10);
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

    private byte[] getBytesPixels() {
        byte[] pixels = new byte[(int) mapWidth * (int) mapHeight * 4];
        mapPixmap.getPixels().get(pixels);
        return pixels;
    }

}
