package com.itique.ps.util.generate;

import com.itique.ps.model.world.Building;
import com.itique.ps.model.world.BuildingType;

import static java.lang.Math.random;

public class BuildingGeneratorUtil {

    public static Building generateBuilding() {
        double random = random();
        return new Building(
                random < 0.45 ? BuildingType.LIVING
                        : random > 0.45 && random < 0.75 ? BuildingType.OFFICE
                        : random > 0.75 && random < 0.77 ? BuildingType.SHOP
                        : BuildingType.MANUFACTURE
        );
    }

}
