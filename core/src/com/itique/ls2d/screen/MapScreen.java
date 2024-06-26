package com.itique.ls2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.viewport.*;
import com.itique.ls2d.constant.world.DefaultCity;
import com.itique.ls2d.custom.actor.CityMapActor;
import com.itique.ls2d.custom.component.ChatComponent;
import com.itique.ls2d.custom.component.style.MapStyle;
import com.itique.ls2d.custom.listener.ControlListener;
import com.itique.ls2d.custom.listener.MapNavigationListener;
import com.itique.ls2d.model.Man;
import com.itique.ls2d.model.timeline.TimeLineTask;
import com.itique.ls2d.model.world.City;
import com.itique.ls2d.model.world.Terrain;
import com.itique.ls2d.model.world.World;
import com.itique.ls2d.service.event.DialogEvent;
import com.itique.ls2d.service.file.CityFileDao;
import com.itique.ls2d.service.file.PersonFileDao;
import com.itique.ls2d.service.file.WorldFileDao;

import java.util.Map;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.itique.ls2d.constant.Constant.GLOBAL_PREF;
import static com.itique.ls2d.constant.Constant.HERO_START_CITY_ID_KEY;
import static com.itique.ls2d.constant.world.DefaultCityFactory.GREEN_ISLAND_CITY;

public class MapScreen implements Screen {

    private static final int DELTA_PX = 20;
    private static final int EDGE_PX = 15;
    private static final float ZOOM_DIVIDER = 5f;
    private static final float ZOOM_MIN = 1.0f / ZOOM_DIVIDER;
    private static final float ZOOM_MAX = ZOOM_MIN * ZOOM_DIVIDER;
    private static final int BORDER_DIAPASON = 5;

    private Game game;
    private Skin skin;
    private Stage stage;
    private TextureAtlas atlas;
    private CityMapActor cityMapActor;
    private Viewport viewport;
    private OrthographicCamera camera;
    private PersonFileDao personDao;
    private WorldFileDao worldDao;
    private CityFileDao cityDao;
    private Man hero;
    private World world;
    private City city;
    private Table controlTable;
    private ChatComponent chat;
    private Container<Table> controlContainer;
    private Label time;
    private Pixmap cityMap;
    private Texture cityMapTexture;
    private Map<Terrain, Pixmap> terrainPixmaps;
    private IntSet keys;
    private TimeLineTask timeline;
    private final AsyncExecutor asyncExecutor;
    private AsyncResult<String> timeString;
    private Music bgMusic;
    private float worldWidth;
    private float worldHeight;
    private final MapNavigationListener mapNavigationListener;

    public MapScreen(Game game) {
        this.game = game;
        initializeData();
        atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        skin = new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage(viewport);
        mapNavigationListener = new MapNavigationListener(camera, stage, new MapStyle());
        time = new Label("00:00", skin);
        controlTable = new Table();
        controlTable.setFillParent(true);
        controlTable.add(time).pad(20).top().right();
        controlTable.row();
        chat = new ChatComponent(hero.getProfile().getName());
        controlTable.add(chat);
        controlContainer = new Container<>(controlTable);
        controlContainer.setFillParent(true);
        controlContainer.center();
        keys = new IntSet();
        asyncExecutor = new AsyncExecutor(1);
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/map_bg.mp3"));
        cityMapActor = new CityMapActor(cityMapTexture);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mapNavigationListener);
        timeline = new TimeLineTask(500L);
        timeString = asyncExecutor.submit(timeline);
        bgMusic.setLooping(true);
        bgMusic.play();
        DialogEvent dialogEvent = new DialogEvent();
        dialogEvent.startDialog();
        chat.startChat(dialogEvent, hero);
        stage.addActor(cityMapActor);
        stage.addActor(controlContainer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.valueOf("178693"));
        processTimLine();
        processKeyDown();
        mapNavigationListener.mouseMoved(Gdx.input.getX(), Gdx.input.getY());
        stage.act(delta);
        stage.draw();
        camera.update();
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
        cityMap.dispose();
        cityMapTexture.dispose();
        timeline.forceFinish();
        asyncExecutor.dispose();
        bgMusic.dispose();
        cityMapActor.dispose();
    }

//    @Override
//    public boolean keyDown(int keycode) {
//        float x = camera.position.x;
//        float y = camera.position.y;
//        if (keys.contains(A)) {
//            camera.position.lerp(new Vector3(x - DELTA_PX, y, 0), 0.1f);
//        }
//        if (keys.contains(W)) {
//            camera.position.lerp(new Vector3(x, y + DELTA_PX, 0), 0.1f);
//        }
//        if (keys.contains(D)) {
//            camera.position.lerp(new Vector3(x + DELTA_PX, y, 0), 0.1f);
//        }
//        if (keys.contains(S)) {
//            camera.position.lerp(new Vector3(x, y - DELTA_PX, 0), 0.1f);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean keyUp(int keycode) {
//        if (this.keys.contains(keycode)) {
//            this.keys.remove(keycode);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean keyTyped(char character) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        return false;
//    }
//
//    @Override
//    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        return false;
//    }
//
//    @Override
//    public boolean mouseMoved(int screenX, int screenY) {
//        processMapNavigation(screenX, screenY);
//        return true;
//    }
//
//    @Override
//    public boolean scrolled(float amountX, float amountY) {
//        OrthographicCamera camera = (OrthographicCamera) controlStage.getCamera();
//        if (Float.compare(camera.zoom, ZOOM_MAX) >= 0) {
//            camera.zoom = ZOOM_MAX - 1.0f / ZOOM_DIVIDER;
//        } else if (Float.compare(camera.zoom, ZOOM_MIN) <= 0) {
//            camera.zoom = ZOOM_MIN + 1.0f / ZOOM_DIVIDER;
//        }
//        if (Float.compare(camera.zoom, ZOOM_MAX) < 0
//                && Float.compare(camera.zoom, ZOOM_MIN) > 0) {
//            camera.zoom += amountY / ZOOM_DIVIDER;
//        }
//        return true;
//    }
//
//    private void processMapNavigation(int screenX, int screenY) {
//        if (screenY > 0 && screenX > 0) {
//            if (screenX < EDGE_PX) {
//                if (screenY > camera.viewportHeight - EDGE_PX) {
//                    camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
//                            camera.position.y - DELTA_PX, 0), 0.1f);
//                } else if (screenY < EDGE_PX) {
//                    camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
//                            camera.position.y + DELTA_PX, 0), 0.1f);
//                } else {
//                    camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
//                            camera.position.y, 0), 0.1f);
//                }
//            } else if (screenX > camera.viewportWidth - EDGE_PX) {
//                if (screenY < EDGE_PX) {
//                    camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
//                            camera.position.y + DELTA_PX, 0), 0.1f);
//                } else if (screenY > camera.viewportHeight - EDGE_PX) {
//                    camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
//                            camera.position.y - DELTA_PX, 0), 0.1f);
//                } else {
//                    camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
//                            camera.position.y, 0), 0.1f);
//                }
//            } else if (screenY < EDGE_PX) {
//                camera.position.lerp(new Vector3(camera.position.x,
//                        camera.position.y + DELTA_PX, 0), 0.1f);
//            } else if (screenY > camera.viewportHeight - EDGE_PX) {
//                camera.position.lerp(new Vector3(camera.position.x,
//                        camera.position.y - DELTA_PX, 0), 0.1f);
//            }
//        }
//    }

    private void processKeyDown() {
        int keyDown = Gdx.app.getInput().isKeyPressed(A) ? A
                : Gdx.app.getInput().isKeyPressed(W) ? W
                : Gdx.app.getInput().isKeyPressed(D) ? D
                : Gdx.app.getInput().isKeyPressed(S) ? S
                : -1;
        if (keyDown != -1) {
            mapNavigationListener.keyDown(keyDown);
        }
    }

    private void drawCityBorders() {
        cityMap.setColor(Color.RED);
        for (int y = 0; y <= cityMap.getHeight(); y += BORDER_DIAPASON) {
            int drawY = y;
            cityMap.drawPixel(0, drawY++);
            cityMap.drawPixel(0, drawY);
        }
        for (int y = 0; y <= cityMap.getHeight(); y += BORDER_DIAPASON) {
            int drawY = y;
            cityMap.drawPixel(cityMap.getWidth() - 1, drawY++);
            cityMap.drawPixel(cityMap.getWidth() - 1, drawY);
        }
        for (int x = 0; x <= cityMap.getHeight(); x += BORDER_DIAPASON) {
            int drawX = x;
            cityMap.drawPixel(drawX++, 0);
            cityMap.drawPixel(drawX, 0);
        }
        for (int x = 0; x <= cityMap.getHeight(); x += BORDER_DIAPASON) {
            int drawX = x;
            cityMap.drawPixel(drawX++, cityMap.getHeight() - 1);
            cityMap.drawPixel(drawX, cityMap.getHeight() - 1);
        }
    }

    private void processTimLine() {
        if (!timeString.isDone()) {
            time.setText(timeline.get());
        } else {
            timeline = new TimeLineTask(500L);
            timeString = asyncExecutor.submit(timeline);
        }
    }

//    private void processControlContainer() {
//        controlContainer.setPosition(viewport.getScreenX(),
//                viewport.getScreenY());
//    }

    private void initializeData() {
        personDao = new PersonFileDao();
        worldDao = new WorldFileDao();
        cityDao = new CityFileDao();
        hero = personDao.findHero().get();
        world = worldDao.findAll().stream()
                .filter(w -> w.getHumansIds().stream().anyMatch(h -> h.equals(hero.getId())))
                .findFirst().get();
        city = cityDao.findById(world.getCitiesIds().stream()
                .filter(id -> id.equals(Gdx.app.getPreferences(GLOBAL_PREF).getString(HERO_START_CITY_ID_KEY)))
                .findFirst().get()).get();
        if (city.getDefaultId().equals(GREEN_ISLAND_CITY.name())) {
            DefaultCity defaultCity = GREEN_ISLAND_CITY.getCity();
            terrainPixmaps = defaultCity.getTerrainTexturesPaths()
                    .entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> new Pixmap(Gdx.files.internal(v.getValue()))));
            cityMap = new Pixmap(Gdx.files.internal(defaultCity.getMapPath()));
            drawCityBorders();
            cityMapTexture = new Texture(cityMap);
            worldWidth = cityMap.getWidth() + 500f;
            worldHeight = cityMap.getHeight() + 500f;
        }
    }
}
