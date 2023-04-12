package com.itique.ls2d.mapeditor.model;

import com.itique.ls2d.model.Identifiable;

import java.io.Serializable;
import java.util.UUID;

public class RoadModel extends Identifiable implements Serializable {

    private float xStart;
    private float yStart;
    private long length;
    private Type type;
    private Direction direction;

    public RoadModel(float xStart, float yStart, long length, Type type, Direction direction) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.length = length;
        this.type = type;
        this.direction = direction;
    }

    public RoadModel(String id, float xStart, float yStart, long length, Type type, Direction direction) {
        super(id);
        this.xStart = xStart;
        this.yStart = yStart;
        this.length = length;
        this.type = type;
        this.direction = direction;
    }

    public RoadModel() {
        // serialization constructor
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

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, TOP_LEFT_BOTTOM_RIGHT, TOP_RIGHT_BOTTOM_LEFT
    }

    public enum Type {
        STRAIGHT, DIAGONAL, PART
    }

}
