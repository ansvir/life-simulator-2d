package com.itique.ls2d.strategy;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Deprecated(since = "Will be used in future releases")
public class Strategy {

    private BuildingStrategy buildingStrategy;
    private HeroStartStrategy heroStartStrategy;
    private RoadStrategy roadStrategy;

    public Strategy(BuildingStrategy buildingStrategy, HeroStartStrategy heroStartStrategy, RoadStrategy roadStrategy) {
        this.buildingStrategy = buildingStrategy;
        this.heroStartStrategy = heroStartStrategy;
        this.roadStrategy = roadStrategy;
    }

    public BuildingStrategy getBuildingStrategy() {
        return buildingStrategy;
    }

    public void setBuildingStrategy(BuildingStrategy buildingStrategy) {
        this.buildingStrategy = buildingStrategy;
    }

    public HeroStartStrategy getHeroStartStrategy() {
        return heroStartStrategy;
    }

    public void setHeroStartStrategy(HeroStartStrategy heroStartStrategy) {
        this.heroStartStrategy = heroStartStrategy;
    }

    public RoadStrategy getRoadStrategy() {
        return roadStrategy;
    }

    public void setRoadStrategy(RoadStrategy roadStrategy) {
        this.roadStrategy = roadStrategy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("buildingStrategy", buildingStrategy)
                .append("heroStartStrategy", heroStartStrategy)
                .append("roadStrategy", roadStrategy)
                .toString();
    }

}
