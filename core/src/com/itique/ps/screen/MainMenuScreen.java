package com.itique.ps.screen;

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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ps.PoliceSimulator;

import static com.itique.ps.constant.Constant.CITIES_DATA;
import static com.itique.ps.constant.Constant.FIRST_PLAY_KEY;
import static com.itique.ps.constant.Constant.GLOBAL_PREF;
import static com.itique.ps.constant.Constant.HUMANS_DATA;
import static com.itique.ps.constant.Constant.WORLDS_DATA;

public class MainMenuScreen implements Screen {

    private PoliceSimulator game;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Table table;
    private Sound clickSound;
    private Music bgMusic;

    public MainMenuScreen(PoliceSimulator game) {
        this.game = game;
        atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        skin = new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, batch);
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio").child("click_sound.mp3"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio").child("menu_bg.ogg"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        bgMusic.setLooping(true);
        bgMusic.play();
        table = new Table();
        table.setFillParent(true);
        table.background(new SpriteDrawable(new Sprite(
                new Texture(Gdx.files.internal("textures/bg.png")))));
        if (!Gdx.app.getPreferences(GLOBAL_PREF).getBoolean(FIRST_PLAY_KEY)) {
            TextButton play = new TextButton("PLAY", skin);
            play.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    clickSound.play();
                    Gdx.files.local(HUMANS_DATA).writeString("[]", false);
                    Gdx.files.local(WORLDS_DATA).writeString("[]", false);
                    Gdx.files.local(CITIES_DATA).writeString("[]", false);
//                    Gdx.app.getPreferences("global").putBoolean("FIRST_PLAY", true);
//                    Gdx.app.getPreferences("global").flush();
                    bgMusic.stop();
                    game.setScreen(new CreateHeroScreen(game));
                }
            });
            table.add(play).expand().bottom().left().pad(20);
        } else {
            TextButton load = new TextButton("LOAD", skin);
            load.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
//                    Gdx.app.getPreferences("global").putBoolean("FIRST_PLAY", true);
//                    Gdx.app.getPreferences("global").flush();
                    bgMusic.stop();
                    game.setScreen(new CityScreen(game));
                }
            });
            table.add(load).bottom().left().pad(20);
        }

        stage.addActor(table);
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
        bgMusic.dispose();
        clickSound.dispose();
    }
}
