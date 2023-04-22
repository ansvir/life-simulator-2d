package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class MapActor extends AbstractGroup {

    private Texture staticTexture;

    public MapActor(Texture staticTexture, Vector2 size) {
        super(new Vector2(0, 0), size);
        this.staticTexture = staticTexture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(staticTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void setStaticTexture(Texture staticTexture) {
        this.staticTexture = staticTexture;
    }

}
