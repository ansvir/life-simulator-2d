package com.itique.ls2d.mapeditor.model;

import com.itique.ls2d.model.Identifiable;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class MapModel extends Identifiable implements Serializable {

    private String name;
    private float width;
    private float height;
    private List<RoadModel> roads;
    private List<BuildingModel> buildings;
    private String mapImageBase64;

    public MapModel(String name, float width, float height, List<RoadModel> roads, List<BuildingModel> buildings, String mapImageBase64) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.roads = roads;
        this.buildings = buildings;
        this.mapImageBase64 = mapImageBase64;
    }

    public MapModel(String id, String name, float width, float height, List<RoadModel> roads, List<BuildingModel> buildings, String mapImageBase64) {
        super(id);
        this.name = name;
        this.width = width;
        this.height = height;
        this.roads = roads;
        this.buildings = buildings;
        this.mapImageBase64 = mapImageBase64;
    }

    public MapModel() {
        // serialization constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<RoadModel> getRoads() {
        return roads;
    }

    public void setRoads(List<RoadModel> roads) {
        this.roads = roads;
    }

    public List<BuildingModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingModel> buildings) {
        this.buildings = buildings;
    }

    public String getMapImageBase64() {
        return mapImageBase64;
    }

    public void setMapImageBase64(String mapImageBase64) {
        this.mapImageBase64 = mapImageBase64;
    }

}
