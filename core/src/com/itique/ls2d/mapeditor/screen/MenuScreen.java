package com.itique.ls2d.mapeditor.screen;

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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itique.ls2d.screen.MainMenuScreen;

public class MenuScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Table table;
    private Sound clickSound;
    private Music bgMusic;
    private TextButton newMap;
    private TextButton loadMap;
    private TextButton back;

    public MenuScreen(Game game) {
        this.game = game;
        atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        skin = new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, batch);
        clickSound = Gdx.audio.newSound(Gdx.files.internal("audio").child("click_sound.mp3"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("audio").child("menu_bg.ogg"));
        table = new Table();
    }

    @Override
    public void show() {
        newMap = new TextButton("NEW", skin);
        loadMap = new TextButton("LOAD", skin);
        back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                bgMusic.stop();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        Gdx.input.setInputProcessor(stage);
        bgMusic.setLooping(true);
        bgMusic.play();
        table = new Table();
        table.setFillParent(true);
        table.background(new SpriteDrawable(new Sprite(
                new Texture(Gdx.files.internal("textures/mapeditor/bg.png")))));
        table.add(newMap).bottom().expand().left().padLeft(20);
        table.row();
        table.add(loadMap).expandX().left().padLeft(20);
        table.add(back).right().pad(20);
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
