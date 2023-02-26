package com.itique.ps.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceUtil {

    public static Texture resizeTexture(Texture texture, int width, int height) {
        texture.getTextureData().prepare();
        Pixmap pixmap = resizePixmap(texture.getTextureData().consumePixmap(), width, height);
        Texture newTexture = new Texture(pixmap);
        pixmap.dispose();
        return newTexture;
    }

    public static Pixmap resizePixmap(Pixmap pixmap, int width, int height) {
        Pixmap newPixmap = new Pixmap(width, height, pixmap.getFormat());
        newPixmap.drawPixmap(pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, newPixmap.getWidth(), newPixmap.getHeight()
        );
        return newPixmap;
    }

    public static Skin getDefaultSkin() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        return new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
    }

}
