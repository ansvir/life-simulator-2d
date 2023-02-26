package com.itique.ls2d.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.constant.world.DefaultCity;
import com.itique.ls2d.custom.actor.HumanActor;
import com.itique.ls2d.custom.component.GridComponent;
import com.itique.ls2d.model.ImageItem;
import com.itique.ls2d.model.Man;
import com.itique.ls2d.model.world.City;
import com.itique.ls2d.model.world.Terrain;
import com.itique.ls2d.model.world.World;
import com.itique.ls2d.service.CityFileDao;
import com.itique.ls2d.service.PersonFileDao;
import com.itique.ls2d.service.WorldFileDao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.itique.ls2d.constant.Constant.GLOBAL_PREF;
import static com.itique.ls2d.constant.Constant.HERO_START_CITY_ID_KEY;
import static com.itique.ls2d.constant.world.DefaultCityFactory.GREEN_ISLAND_CITY;

public class MapScreen implements Screen, InputProcessor {

    private static final int DELTA_PX = 10;
    private static final int EDGE_PX = 8;
    private static final float ZOOM_MAX = 1.0f;
    private static final float ZOOM_MIN = 0.0f;

    private Game game;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    private PersonFileDao personDao;
    private WorldFileDao worldDao;
    private CityFileDao cityDao;
    private Man hero;
    private World world;
    private City city;
    private Container<Table> controlContainer;
    private Pixmap cityMap;
    private Texture cityMapTexture;
    private Map<Terrain, Pixmap> terrainPixmaps;

    public MapScreen(Game game) {
        this.game = game;
        stage = new Stage();
        atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        skin = new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, batch);
        personDao = new PersonFileDao();
        worldDao = new WorldFileDao();
        cityDao = new CityFileDao();
        controlContainer = new Container<>();
        controlContainer.setFillParent(true);
        controlContainer.center();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
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
            cityMapTexture = new Texture(cityMap);
            viewport.setWorldWidth(cityMap.getWidth());
            viewport.setWorldHeight(cityMap.getHeight());
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        Gdx.input.setInputProcessor(this);
        camera.update();
        batch.begin();
        batch.draw(cityMapTexture, 0, 0);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }

    @Override
    public boolean keyDown(int keycode) {
        float x = camera.position.x;
        float y = camera.position.y;
        switch (keycode) {
            case A: camera.position.lerp(new Vector3(x - DELTA_PX, y, 0), 0.1f);
            case W: camera.position.lerp(new Vector3(x, y - DELTA_PX, 0), 0.1f);
            case D: camera.position.lerp(new Vector3(x + DELTA_PX, y, 0), 0.1f);
            case S: camera.position.lerp(new Vector3(x, y + DELTA_PX, 0), 0.1f);
        }
        return true;
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
        System.out.println("screenX = " + screenX + ", screenY = " + screenY);
        processMapNavigation(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (Float.compare(camera.zoom, ZOOM_MAX) < 0
                && Float.compare(camera.zoom, ZOOM_MIN) > 0) {
            camera.zoom += amountY / 4;
            System.out.println(camera.zoom);
        }
        return true;
    }

    private void processMapNavigation(int screenX, int screenY) {
        if (screenX < 1 && screenY > camera.viewportHeight - 1) {
            camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
                    camera.position.y + DELTA_PX, 0), 0.1f);
        } else if (screenX < 1 && screenY < 1) {
            camera.position.lerp(new Vector3(camera.position.x - DELTA_PX,
                    camera.position.y - DELTA_PX, 0), 0.1f);
        } else if (screenX > camera.viewportWidth - 1 && screenY < 1) {
            camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
                    camera.position.y - DELTA_PX, 0), 0.1f);
        } else if (screenX > camera.viewportWidth - 1 && screenY > camera.viewportHeight - 1) {
            camera.position.lerp(new Vector3(camera.position.x + DELTA_PX,
                    camera.position.y + DELTA_PX, 0), 0.1f);
        }
    }
}