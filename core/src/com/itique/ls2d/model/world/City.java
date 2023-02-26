package com.itique.ls2d.model.world;

import com.itique.ls2d.model.Identifiable;
import com.itique.ls2d.model.place.Place;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class City extends Identifiable implements Serializable {

    private String defaultId;
    private String name;
    private float squareKm;
    private int population;
    private List<Building> buildings;

    public City(String name, float squareKm, int population, List<Building> buildings) {
        this.defaultId = super.getId();
        this.name = name;
        this.squareKm = squareKm;
        this.population = population;
        this.buildings = buildings;
    }

    public City(String defaultId, String name, float squareKm, int population, List<Building> buildings) {
        this.defaultId = defaultId;
        this.name = name;
        this.squareKm = squareKm;
        this.population = population;
        this.buildings = buildings;
    }

    public City() {
        // for serialization
    }

    public String getDefaultId() {
        return defaultId;
    }

    public City setDefaultId(String defaultId) {
        this.defaultId = defaultId;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSquareKm() {
        return squareKm;
    }

    public void setSquareKm(float squareKm) {
        this.squareKm = squareKm;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", super.getId())
                .append("defaultId", defaultId)
                .append("name", name)
                .append("squareKm", squareKm)
                .append("population", population)
                .append("buildings", buildings)
                .toString();
    }
}
