package com.itique.ls2d.mapeditor.model;

import com.itique.ls2d.model.Identifiable;
import com.itique.ls2d.model.world.BuildingType;

import java.io.Serializable;

public class BuildingModel extends AbstractShape implements Serializable {

    private BuildingType type;

    public BuildingModel(BuildingType type, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.type = type;
    }

    public BuildingModel() {
        // serialization constructor
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

}
