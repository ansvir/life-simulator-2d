package com.itique.ls2d.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.constant.world.DefaultGreenCity;
import com.itique.ls2d.constant.world.DefaultCity;
import com.itique.ls2d.model.Man;
import com.itique.ls2d.model.Profile;
import com.itique.ls2d.model.world.City;
import com.itique.ls2d.model.world.World;
import com.itique.ls2d.service.file.CityFileDao;
import com.itique.ls2d.service.file.PersonFileDao;
import com.itique.ls2d.service.file.WorldFileDao;
import com.itique.ls2d.util.ResourceUtil;
import com.itique.ls2d.util.generate.BodyCustomizatorUtil;
import com.itique.ls2d.util.generate.PersonGeneratorUtil;
import com.itique.ls2d.util.generate.WorldGeneratorUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.itique.ls2d.constant.Constant.GLOBAL_PREF;
import static com.itique.ls2d.constant.Constant.HERO_ID_KEY;
import static com.itique.ls2d.constant.Constant.HERO_START_CITY_ID_KEY;

public class CreateHeroScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Table table;
    private Table heroTable;
    private Man hero;
    private World world;
    private City city;
    private DefaultCity defaultCity;
    private Image heroImage;
    private Texture heroTexture;
    private SpriteDrawable heroSprite;
    private TextButton bodyButton;
    private TextButton skinButton;
    private TextButton hairButton;
    private Window customizingModal;
    private Window biographyModal;
    private Window worldWindow;
    private TextField heroName;
    private TextField heroAge;
    private SelectBox<String> defaultCities;
    private Music bgMusic;
    private Sound clickSound;
    private Map<String, City> defaultCitiesMap;
    private BodyCustomizatorUtil customizator;

    public CreateHeroScreen(Game game) {
        this.game = game;
        atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        skin = new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, batch);
        hero = PersonGeneratorUtil.generatePerson();
        defaultCity = new DefaultGreenCity();
        defaultCitiesMap = new HashMap<>();
        customizator = new BodyCustomizatorUtil();
        world = WorldGeneratorUtil.generateEmptyWorld();
        heroTexture = PersonGeneratorUtil.buildManTexture(hero);
        heroSprite = new SpriteDrawable(new Sprite(heroTexture));
        heroImage = new Image(heroSprite);
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio").child("menu_bg.ogg"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio").child("click_sound.mp3"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        bgMusic.setLooping(true);
        bgMusic.play();
        stage.addActor(createMenu());
        stage.addActor(createCustomizingModal());
        stage.addActor(createBiographyModal());
        stage.addActor(createWorldWindow());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        heroTexture.dispose();
        bgMusic.dispose();
        clickSound.dispose();
        customizator.dispose();
    }

    private Table createMenu() {
        table = new Table();
        table.setFillParent(true);
        table.background(new SpriteDrawable(new Sprite(
                new Texture(Gdx.files.internal("textures/bg_wardrobe.png")))));
        Label title = new Label("Hero Creation", skin);
        ImageButton body = getBody();

        table.add(title).expand().left().top().pad(20);
        table.add(body).size(viewport.getScreenHeight() / 2f - body.getHeight(),
                        viewport.getScreenWidth() / 2f)
                .right();
        table.row();
        table.add(getBiography()).left();
        table.add(getWorld()).center();
        table.row();
        table.add(getFinish()).colspan(2).pad(20).center();
        return table;
    }

    private Window createBiographyModal() {
        biographyModal = new Window("BIOGRAPHY", skin);
        biographyModal.setModal(true);
        biographyModal.setSize(viewport.getScreenWidth(), viewport.getScreenHeight());
        biographyModal.setPosition(viewport.getScreenWidth() / 2f - biographyModal.getWidth() / 2f,
                viewport.getScreenHeight() / 2f - biographyModal.getHeight() / 2f);
        ImageButton close = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture(Gdx.files.internal("textures/close.png")))));
        biographyModal.getTitleTable().add(close);
        Label heroNameLabel = new Label("NAME ", skin);
        heroName = new TextField(hero.getProfile().getName(), skin);
        Label heroAgeLabel = new Label("AGE ", skin);
        heroAge = new TextField(String.valueOf(hero.getProfile().getBiology().getAge()), skin);
        TextButton saveChanges = new TextButton("SAVE", skin);
        saveChanges.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                Profile heroProfile = hero.getProfile();
                heroProfile.setName(heroName.getText());
                heroProfile.getBiology().setAge(Integer.parseInt(heroAge.getText()));
                biographyModal.setVisible(false);
            }
        });
        biographyModal.add(heroNameLabel).center();
        biographyModal.add(heroName);
        biographyModal.row();
        biographyModal.add(heroAgeLabel).center();
        biographyModal.add(heroAge);
        biographyModal.row();
        biographyModal.add(saveChanges).colspan(2).pad(20).center();
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                biographyModal.setVisible(false);
            }
        });
        biographyModal.setVisible(false);
        return biographyModal;
    }

    private Window createCustomizingModal() {
        customizingModal = new Window("CUSTOMIZE", skin);
        customizingModal.setModal(true);
        customizingModal.setSize(viewport.getScreenWidth(), viewport.getScreenHeight());
        customizingModal.setPosition(viewport.getScreenWidth() / 2f - customizingModal.getWidth() / 2f,
                viewport.getScreenHeight() / 2f - customizingModal.getHeight() / 2f);
        ImageButton close = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture(Gdx.files.internal("textures/close.png")))));
        customizingModal.getTitleTable().add(close);
        bodyButton = new TextButton("BODY", skin);
        bodyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                hero.getProfile().getBiology().setBody(customizator.nextBody());
                heroTexture = PersonGeneratorUtil.buildManTexture(hero);
                heroSprite = new SpriteDrawable(new Sprite(heroTexture));
                heroImage.setDrawable(heroSprite);
            }
        });
        skinButton = new TextButton("SKIN", skin);
        skinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                hero.getProfile().getBiology().setSkin(customizator.nextSkin());
                heroTexture = PersonGeneratorUtil.buildManTexture(hero);
                heroSprite = new SpriteDrawable(new Sprite(heroTexture));
                heroImage.setDrawable(heroSprite);
            }
        });
        hairButton = new TextButton("HAIR", skin);
        hairButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                hero.getProfile().getBiology().setHair(customizator.nextHair());
                heroTexture = PersonGeneratorUtil.buildManTexture(hero);
                heroSprite = new SpriteDrawable(new Sprite(heroTexture));
                heroImage.setDrawable(heroSprite);
            }
        });
        customizingModal.add(createHeroTable());
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                customizingModal.setVisible(false);
            }
        });
        customizingModal.setVisible(false);
        return customizingModal;
    }

    public Window createWorldWindow() {
        worldWindow = new Window("WORLD", skin);
        worldWindow.setModal(true);
        worldWindow.setSize(viewport.getScreenWidth(), viewport.getScreenHeight());
        worldWindow.setPosition(viewport.getScreenWidth() / 2f - worldWindow.getWidth() / 2f,
                viewport.getScreenHeight() / 2f - worldWindow.getHeight() / 2f);
        ImageButton close = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture(Gdx.files.internal("textures/close.png")))));
        worldWindow.getTitleTable().add(close);
        Label cityNameLabel = new Label("Start City ", skin);
        defaultCities = new SelectBox<>(skin);

        TextButton saveChanges = new TextButton("SAVE", skin);
        saveChanges.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                city = defaultCitiesMap.get(defaultCities.getSelected());
                worldWindow.setVisible(false);
            }
        });
        worldWindow.add(cityNameLabel).center();
        worldWindow.add(defaultCities);
        worldWindow.row();
        worldWindow.add(saveChanges).colspan(2).pad(20).center();
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                worldWindow.setVisible(false);
            }
        });
        worldWindow.setVisible(false);
        return worldWindow;
    }

    private Table createHeroTable() {
        heroTable = new Table();
        Table buttonsTable = new Table();
        buttonsTable.add(bodyButton).spaceLeft(100).center().pad(20);
        buttonsTable.row();
        buttonsTable.add(skinButton).spaceLeft(100).center().pad(20);
        buttonsTable.row();
        buttonsTable.add(hairButton).spaceLeft(100).center().pad(20);
        heroTable.add(heroImage).size(heroImage.getWidth() * 3, heroImage.getHeight() * 3);
        heroTable.add(buttonsTable).center();
        return heroTable;
    }

    private TextButton getFinish() {
        TextButton finish = new TextButton("FINISH", skin);
        finish.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.getPreferences(GLOBAL_PREF).putString(HERO_ID_KEY, hero.getId());
                Gdx.app.getPreferences(GLOBAL_PREF).flush();
                PersonFileDao personDao = new PersonFileDao();
                personDao.save(hero);
                CityFileDao cityDao = new CityFileDao();
                cityDao.save(city);
                Gdx.app.getPreferences(GLOBAL_PREF).putString(HERO_START_CITY_ID_KEY, city.getId());
                Gdx.app.getPreferences(GLOBAL_PREF).flush();
                WorldFileDao worldDao = new WorldFileDao();
                world.setHumansIds(List.of(hero.getId()));
                world.setCitiesIds(List.of(city.getId()));
                worldDao.save(world);
                bgMusic.stop();
                game.setScreen(new MapScreen(game));
            }
        });
        return finish;
    }

    private ImageButton getBiography() {
        ImageButton biography = new ImageButton(new SpriteDrawable(new Sprite(
                ResourceUtil.resizeTexture(
                        new Texture(Gdx.files.internal("textures/book.png")),
                        64, 64))));
        biography.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                biographyModal.setVisible(true);
            }
        });
        return biography;
    }

    private ImageButton getBody() {
        ImageButton body = new ImageButton(new SpriteDrawable(new Sprite(
                ResourceUtil.resizeTexture(
                        new Texture(Gdx.files.internal("textures/body_icon.png")),
                        64, 64))));
        body.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                customizingModal.setVisible(true);
            }
        });
        return body;
    }

    private ImageButton getWorld() {
        ImageButton world = new ImageButton(new SpriteDrawable(new Sprite(
                ResourceUtil.resizeTexture(
                        new Texture(Gdx.files.internal("textures/planet_icon.png")),
                        64, 64))));
        world.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                defaultCitiesMap.clear();
                city = defaultCity.generate();
                defaultCities.setItems(city.getName());
                defaultCities.setSelected(city.getName());
                defaultCitiesMap.put(city.getName(), city);
                clickSound.play();
                worldWindow.setVisible(true);
            }
        });
        return world;
    }

}
