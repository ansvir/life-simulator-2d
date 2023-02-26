package com.itique.ps.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ps.constant.Constant;
import com.itique.ps.constant.world.DefaultCity;
import com.itique.ps.control.KeyboardControl;
import com.itique.ps.custom.LerpCamera;
import com.itique.ps.custom.actor.HumanActor;
import com.itique.ps.custom.component.GridComponent;
import com.itique.ps.model.ImageItem;
import com.itique.ps.model.Man;
import com.itique.ps.model.world.City;
import com.itique.ps.model.world.Terrain;
import com.itique.ps.model.world.World;
import com.itique.ps.service.CityFileDao;
import com.itique.ps.service.PersonFileDao;
import com.itique.ps.service.WorldFileDao;
import com.itique.ps.util.ResourceUtil;
import com.itique.ps.util.generate.CityGeneratorUtil;
import com.itique.ps.util.generate.setting.AreaSettings;
import com.itique.ps.util.validator.HumanValidatorUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SHIFT_LEFT;
import static com.badlogic.gdx.Input.Keys.W;
import static com.itique.ps.constant.Constant.GLOBAL_PREF;
import static com.itique.ps.constant.Constant.HERO_START_CITY_ID_KEY;
import static com.itique.ps.constant.world.DefaultCityFactory.GREEN_ISLAND_CITY;

public class CityScreen implements Screen, InputProcessor {

    private Game game;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Man hero;
    private HumanActor heroActor;
    private World world;
    private City city;
    private PersonFileDao personDao;
    private WorldFileDao worldDao;
    private CityFileDao cityDao;
    private Map<Terrain, Pixmap> terrainPixmaps;
    private Pixmap cityMap;
    private Texture cityMapTexture;
    private Texture cityTexture;
    private int heroX;
    private int heroY;
    private int zoom;
    private AreaSettings settings;
    private KeyboardControl control;
    private Sound footstepsSound;
    private IntSet keysSet;
    private long millisSoundEnd;
    private boolean soundPlaying;
    private TextButton button;
    private GridComponent inventory;
    private Container<Table> controlContainer;
    private LerpCamera lerpCamera;

    public CityScreen(Game game) {
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
        footstepsSound = Gdx.audio.newSound(Gdx.files.internal("audio").child("footsteps.mp3"));
        keysSet = new IntSet();
        millisSoundEnd = 0L;
        soundPlaying = false;
        this.button = new TextButton("INVENTORY", skin);
        this.inventory = new GridComponent("Inventory",
                List.of(new ImageItem("ITEM", "ITEM", new Texture("textures/book.png"))),
                () -> System.out.println("Hello World!"),
                5);
        this.inventory.setVisible(true);
        this.button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventory.setVisible(!inventory.isVisible());
            }
        });
        Table mainTable = new Table();
        mainTable.center();
        mainTable.add(this.button);
        mainTable.add(this.inventory).center();
        this.controlContainer = new Container<>(mainTable);
        this.controlContainer.setFillParent(true);
        this.controlContainer.center();
        this.controlContainer.debugAll();
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
            this.terrainPixmaps = defaultCity.getTerrainTexturesPaths()
                    .entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> new Pixmap(Gdx.files.internal(v.getValue()))));
            this.cityMap = new Pixmap(Gdx.files.internal(defaultCity.getMapPath()));
        }
        heroX = (int) (city.getSquareKm() * 1000 / 2);
        heroY = (int) (city.getSquareKm() * 1000 / 2);
        this.camera.translate(0, 0);
        zoom = 0;
        heroActor = new HumanActor();
        control = new KeyboardControl(heroX, heroY, 90, Constant.TILE_WIDTH / 2, 10, 2, 2);
        lerpCamera = new LerpCamera(camera);
        settings = new AreaSettings(viewport.getScreenWidth(), viewport.getScreenHeight(),
                terrainPixmaps, Constant.TILE_WIDTH + zoom, cityMap, heroX, heroY,
                new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        stage.addActor(heroActor);
//        stage.addActor(controlContainer);
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(this);
        ScreenUtils.clear(Color.WHITE);
        stage.act(delta);
        this.control.updateMotion(delta);
        playFootsteps();
        this.heroActor.setX(stage.getViewport().getScreenWidth() / 2f);
        this.heroActor.setY(stage.getViewport().getScreenHeight() / 2f);
        this.heroActor.setRotation(this.control.getRotation());
        this.settings.setHeroX(this.control.getX());
        this.settings.setHeroY(this.control.getY());
        cityTexture = CityGeneratorUtil.generateCityArea(this.settings);
        batch.begin();
        batch.draw(cityTexture, 0, 0);
        createMinimap();
        batch.draw(cityMapTexture, stage.getViewport().getScreenX() + 10,
                stage.getViewport().getScreenY() + stage.getViewport().getScreenHeight()
                        - cityMapTexture.getHeight() - 10);
        batch.end();
        stage.draw();
        this.camera.update();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        this.settings.setViewportWidth(width);
        this.settings.setViewportHeight(height);
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
        stage.dispose();
        skin.dispose();
        atlas.dispose();
        batch.dispose();
        terrainPixmaps.values().forEach(Pixmap::dispose);
        cityMap.dispose();
        cityTexture.dispose();
        cityMapTexture.dispose();
        footstepsSound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        this.keysSet.add(keycode);
        updateControl(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        this.keysSet.remove(keycode);
        if (keycode == W || keycode == A || keycode == S || keycode == D
                || keycode == SHIFT_LEFT) {
            if ((keycode == W && control.isUp()) || (keycode == S && control.isDown())
                    || (keycode == A && control.isLeft()) || (keycode == D && control.isRight())) {
                control.stop();
                heroActor.setMovement(HumanActor.Movement.STAND);
            } else if (keycode == W && (control.isMove45() || control.isMove135())) {
                control.stop();
                heroActor.setMovement(HumanActor.Movement.STAND);
            } else if (keycode == D && (control.isMove135() || control.isMove225())) {
                control.stop();
                heroActor.setMovement(HumanActor.Movement.STAND);
            } else if (keycode == S && (control.isMove225() || control.isMove315())) {
                control.stop();
                heroActor.setMovement(HumanActor.Movement.STAND);
            } else if (keycode == A && (control.isMove315() || control.isMove45())) {
                control.stop();
                heroActor.setMovement(HumanActor.Movement.STAND);
            }
            lerpCamera.moveForward();
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
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void playFootsteps() {
        if ((this.keysSet.contains(W) || this.keysSet.contains(A) || this.keysSet.contains(D)
                || this.keysSet.contains(S)) && !this.soundPlaying) {
            this.millisSoundEnd = System.currentTimeMillis() + 450L;
            this.footstepsSound.play();
            this.soundPlaying = true;
        } else if (this.soundPlaying && this.millisSoundEnd <= System.currentTimeMillis()) {
            this.footstepsSound.stop();
            this.soundPlaying = false;
        }
    }

    // TODO fix
    private void createMinimap() {
        int cityMapWidth = (int) (stage.getViewport().getScreenWidth() * 5 *
                (stage.getViewport().getScreenWidth() * 5 / (cityMap.getWidth() * 100.0f)));
        int cityMapHeight = (int) (stage.getViewport().getScreenHeight() * 5 *
                (stage.getViewport().getScreenHeight() * 5 / (cityMap.getHeight() * 100.0f)));
        Texture minimapTexture = new Texture(ResourceUtil.resizePixmap(cityMap, cityMapWidth, cityMapHeight));
        if (!minimapTexture.getTextureData().isPrepared()) {
            minimapTexture.getTextureData().prepare();
        }
        int citySquareM = (int) (city.getSquareKm() * 1000.0f);
//        float viewportWidthM = citySquareM * (citySquareM / (stage.getViewport().getScreenWidth() * 100.0f));
//        float viewportHeightM = citySquareM * (citySquareM / (stage.getViewport().getScreenHeight() * 100.0f));
        float viewportWidth = stage.getViewport().getScreenWidth()
                * (stage.getViewport().getScreenWidth() / (cityMapWidth * 100.0f));
        float viewportHeight = stage.getViewport().getScreenHeight()
                * (stage.getViewport().getScreenHeight() / (cityMapHeight * 100.0f));
        Pixmap minimap = minimapTexture.getTextureData().consumePixmap();
        minimap.setColor(Color.RED);
        int areaX = (int) (cityMapWidth - this.settings.getHeroX() / 100 - viewportWidth / 2);
        int areaY = (int) (cityMapHeight - this.settings.getHeroY() / 100 - viewportHeight / 2);
//        int areaX = (int) (citySquareM - this.settings.getHeroX() - viewportWidthM / 2);
//        int areaY = (int) (citySquareM - this.settings.getHeroY() - viewportHeightM / 2);
        minimap.drawRectangle(areaX, areaY, (int) viewportWidth, (int) viewportHeight);
        cityMapTexture = new Texture(minimap);
        minimap.dispose();
        minimapTexture.dispose();
    }

    private void updateControl(int keycode) {
        if (keycode == W || keycode == A || keycode == S || keycode == D
                || keycode == SHIFT_LEFT) {
            if (this.keysSet.size >= 2) {
                if (keycode == W) {
                    if (this.keysSet.contains(A)) {
                        HumanValidatorUtil.validate(control, cityMapTexture);
                        control.move45();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    } else if (this.keysSet.contains(D)) {
                        control.move135();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    }
                } else if (keycode == D) {
                    if (this.keysSet.contains(W)) {
                        control.move135();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    } else if (this.keysSet.contains(S)) {
                        control.move225();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    }
                } else if (keycode == S) {
                    if (this.keysSet.contains(D)) {
                        control.move225();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    } else if (this.keysSet.contains(A)) {
                        control.move315();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    }
                } else if (keycode == A) {
                    if (this.keysSet.contains(S)) {
                        control.move315();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    } else if (this.keysSet.contains(W)) {
                        control.move45();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    }
                }
                lerpCamera.moveBack(this.heroActor.getX() + this.control.getXDelta(),
                        this.heroActor.getY() + this.control.getYDelta());
            } else {
                switch (keycode) {
                    case SHIFT_LEFT:
                        control.sprint();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                    case W:
                        control.up();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                        break;
                    case S:
                        control.down();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                        break;
                    case A:
                        control.left();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                        break;
                    case D:
                        control.right();
                        heroActor.setMovement(HumanActor.Movement.WALK);
                        break;
                }
                lerpCamera.moveBack(this.heroActor.getX() + this.control.getXDelta(),
                        this.heroActor.getY() + this.control.getYDelta());
            }
        }
    }

}
