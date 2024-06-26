package com.itique.ls2d.model.world;

import com.itique.ls2d.model.Identifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.UUID;

public class World extends Identifiable {

    private String planetName;
    private int planetDiameterKm;
    private List<String> citiesIds;
    private List<String> humansIds;

    public World(String planetName, int planetDiameterKm, List<String> citiesIds, List<String> humansIds) {
        this.planetName = planetName;
        this.planetDiameterKm = planetDiameterKm;
        this.citiesIds = citiesIds;
        this.humansIds = humansIds;
    }

    public World() {
        // for serialization
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public int getPlanetDiameterKm() {
        return planetDiameterKm;
    }

    public void setPlanetDiameterKm(int planetDiameterKm) {
        this.planetDiameterKm = planetDiameterKm;
    }

    public List<String> getCitiesIds() {
        return citiesIds;
    }

    public void setCitiesIds(List<String> citiesIds) {
        this.citiesIds = citiesIds;
    }

    public List<String> getHumansIds() {
        return humansIds;
    }

    public void setHumansIds(List<String> humansIds) {
        this.humansIds = humansIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", super.getId())
                .append("planetName", planetName)
                .append("planetDiameterKm", planetDiameterKm)
                .append("cities", citiesIds)
                .append("humans", humansIds)
                .toString();
    }

}
