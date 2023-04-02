package com.itique.ls2d.mapeditor.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.itique.ls2d.constant.world.DefaultCity;

public class EditorScreen implements Screen {

    private Game game;
    private Skin skin;
    private TextureAtlas atlas;
    private Stage stage;
    private SplitPane splitPane;
    private Table mainContainer;
    private Image mapImage;
    private Texture mapTexture;
    private DefaultCity defaultCity;

    public EditorScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        skin = new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
        stage = new Stage();
        mainContainer = new Table();
//        mapTexture = new Texture();
        mapImage = new Image();
//        splitPane = new SplitPane(skin);
    }

    @Override
    public void render(float delta) {

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
}
