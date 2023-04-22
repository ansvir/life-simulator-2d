package com.itique.ls2d.mapeditor.model;

import java.io.Serializable;

public class RoadModel extends AbstractShape implements Serializable {

    private Type type;

    public RoadModel(float x, float y, float width, float height, Type type) {
        super(x, y, width, height);
        this.type = type;
    }

    public RoadModel() {
        // serialization constructor
    }

    public static Builder builder() {
        return new Builder();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        STRAIGHT, DIAGONAL, PART
    }

    public static class Builder {

        private float x;
        private float y;
        private float width;
        private float height;
        private Type type;

        public Builder() {
        }

        public Builder x(float xStart) {
            this.x = xStart;
            return this;
        }

        public Builder y(float yStart) {
            this.y = yStart;
            return this;
        }

        public Builder width(float width) {
            this.width = width;
            return this;
        }

        public Builder height(float height) {
            this.height = height;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public RoadModel build() {
            return new RoadModel(this.x, this.y, this.width, this.height, this.type);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public Type getType() {
            return type;
        }

    }

}
