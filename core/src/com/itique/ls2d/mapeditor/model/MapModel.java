package com.itique.ls2d.mapeditor.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class MapModel implements Serializable {

    private String id;
    private String name;
    private List<RoadModel> roads;
    private List<BuildingModel> buildings;
    private String mapImageBase64;

    public MapModel(String name, List<RoadModel> roads, List<BuildingModel> buildings, String mapImageBase64) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.roads = roads;
        this.buildings = buildings;
        this.mapImageBase64 = mapImageBase64;
    }

    public MapModel(String id, String name, List<RoadModel> roads, List<BuildingModel> buildings, String mapImageBase64) {
        this.id = id;
        this.name = name;
        this.roads = roads;
        this.buildings = buildings;
        this.mapImageBase64 = mapImageBase64;
    }

    public MapModel() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
