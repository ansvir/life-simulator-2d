package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class MapActor extends AbstractMapGroup {

    private final Pixmap staticMapPixmap;

    public MapActor(Pixmap staticMapPixmap, Vector2 size) {
        super(new Vector2(0, 0), size);
        this.staticMapPixmap = staticMapPixmap;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(new Texture(staticMapPixmap), getX(), getY(), getWidth(), getHeight());
    }

}
