package com.itique.ps.model.world;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;

public class Building implements Serializable {

    private String id;
    private BuildingType buildingType;
    private String texturePath;

    public Building(BuildingType buildingType) {
        this.id = UUID.randomUUID().toString();
        this.buildingType = buildingType;
        this.texturePath = "textures/terrain/building.png";
    }

    public Building() {
        // for serialization
    }

    public String getId() {
        return id;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("buildingType", buildingType)
                .append("texturePath", texturePath)
                .toString();
    }
}
