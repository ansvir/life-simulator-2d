package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.UUID;

public abstract class AbstractGroup extends Group {

    private final String id;

    public AbstractGroup(String id, Vector2 position, Vector2 size) {
        this.id = id;
        super.setPosition(position.x, position.y);
        super.setSize(size.x, size.y);
    }

    public AbstractGroup(Vector2 position, Vector2 size) {
        this.id = UUID.randomUUID().toString();
        super.setPosition(position.x, position.y);
        super.setSize(size.x, size.y);
    }

    public String getId() {
        return id;
    }

}
