package com.itique.ls2d.mapeditor.model;

import com.itique.ls2d.model.world.BuildingType;

import java.io.Serializable;

public class BuildingModel implements Serializable {

    private BuildingType type;
    private float xStart;
    private float yStart;
    private float width;
    private float height;

    public BuildingModel(BuildingType type, float xStart, float yStart, float width, float height) {
        this.type = type;
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
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

    public float getxStart() {
        return xStart;
    }

    public void setxStart(float xStart) {
        this.xStart = xStart;
    }

    public float getyStart() {
        return yStart;
    }

    public void setyStart(float yStart) {
        this.yStart = yStart;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
