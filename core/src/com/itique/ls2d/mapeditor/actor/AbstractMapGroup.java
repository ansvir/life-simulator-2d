package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.UUID;

public abstract class AbstractMapGroup extends Group {

    private String id;
    private Vector2 position;
    private Vector2 size;

    public AbstractMapGroup(String id, Vector2 position, Vector2 size) {
        this.id = id;
        this.position = position;
        this.size = size;
        setPosition(position.x, position.y);
        setSize(size.x, size.y);
    }

    public AbstractMapGroup(Vector2 position, Vector2 size) {
        this.id = UUID.randomUUID().toString();
        this.position = position;
        this.size = size;
        setPosition(position.x, position.y);
        setSize(size.x, size.y);
    }

    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }
}
