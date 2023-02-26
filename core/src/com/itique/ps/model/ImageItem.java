package com.itique.ps.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ImageItem extends Item {

    private Texture image;

    public ImageItem(String name, String description, Texture image) {
        super(name, description);
        this.image = image;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

}
