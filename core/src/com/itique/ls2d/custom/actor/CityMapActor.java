package com.itique.ls2d.custom.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class CityMapActor extends Actor implements Disposable {

    private Texture texture;

    public CityMapActor(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, 0, 0);
    }


    @Override
    public void dispose() {
        texture.dispose();
    }
}
