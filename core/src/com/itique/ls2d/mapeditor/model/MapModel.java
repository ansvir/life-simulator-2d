package com.itique.ls2d.mapeditor.model;

import java.util.List;

public class MapModel {

    private List<BuildingModel> buildings;

    public MapModel(List<BuildingModel> buildings) {
        this.buildings = buildings;
    }

    public List<BuildingModel> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingModel> buildings) {
        this.buildings = buildings;
    }

}
