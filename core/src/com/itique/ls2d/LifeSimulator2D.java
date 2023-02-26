package com.itique.ls2d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.itique.ls2d.screen.MainMenuScreen;

public class LifeSimulator2D extends Game {


	private Pixmap pixmap;

	@Override
	public void create () {
		pixmap = new Pixmap(Gdx.files.internal("textures/cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		ScreenUtils.clear(Color.WHITE);
		super.render();
	}

	@Override
	public void dispose() {
		pixmap.dispose();
	}
}
