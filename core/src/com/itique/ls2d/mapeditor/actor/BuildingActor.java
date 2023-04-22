package com.itique.ls2d.mapeditor.actor;

import com.badlogic.gdx.math.Vector2;
import com.itique.ls2d.model.world.BuildingType;

public class BuildingActor extends AbstractGroup {

    private BuildingType type;

    public BuildingActor(String id, Vector2 position, Vector2 size, BuildingType type, float xStart, float yStart, float width, float height) {
        super(id, position, size);
        this.type = type;
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

}
