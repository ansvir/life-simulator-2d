package com.itique.ls2d.mapeditor.model;

import com.itique.ls2d.model.world.BuildingType;

public class BuildingModel {

    private BuildingType type;
    private float x;
    private float y;
    private float width;
    private float height;

    public BuildingModel(BuildingType type, float x, float y, float width, float height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
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
