package com.itique.ls2d.constant.world;

import com.itique.ls2d.model.world.City;
import com.itique.ls2d.model.world.Terrain;
import com.itique.ls2d.util.generate.CityGeneratorUtil;
import com.itique.ls2d.util.generate.NameGeneratorUtil;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.random;

public class DefaultGreenCity implements DefaultCity {

    private static final float CITY_SQUARE_KM = 9.1f;
    private static final int POPULATION_FROM = 1000;
    private static final int POPULATION_TO = 5000;
    private static final NameGeneratorUtil.Config NAME_CONFIG = NameGeneratorUtil.Config
            .builder().capitalize().build();

    @Override
    public City generate() {
        return CityGeneratorUtil.generateCity(
                DefaultCityFactory.GREEN_ISLAND_CITY.name(),
                NameGeneratorUtil.generateName(12, NAME_CONFIG),
                CITY_SQUARE_KM,
                (int) (random() * POPULATION_FROM + POPULATION_TO));
    }

    @Override
    public Map<Terrain, String> getTerrainTexturesPaths() {
        Map<Terrain, String> terrainPaths = new HashMap<>();
        terrainPaths.put(Terrain.SAND, "textures/terrain/sand.png");
        terrainPaths.put(Terrain.GRASS, "textures/terrain/grass.png");
        terrainPaths.put(Terrain.ROCK, "textures/terrain/rock.png");
        terrainPaths.put(Terrain.OFFICE, "textures/terrain/office.png");
        terrainPaths.put(Terrain.TREE, "textures/terrain/tree.png");
        terrainPaths.put(Terrain.WATER, "textures/terrain/water.png");
        terrainPaths.put(Terrain.ROAD, "textures/terrain/road.png");
        terrainPaths.put(Terrain.HILL_1, "textures/terrain/hill_1.png");
        terrainPaths.put(Terrain.HILL_2, "textures/terrain/hill_2.png");
        terrainPaths.put(Terrain.HILL_3, "textures/terrain/hill_3.png");
        terrainPaths.put(Terrain.HILL_4, "textures/terrain/hill_4.png");
        terrainPaths.put(Terrain.HILL_5, "textures/terrain/hill_5.png");
        terrainPaths.put(Terrain.HILL_6, "textures/terrain/hill_6.png");
        return terrainPaths;
    }

    @Override
    public String getMapPath() {
        return "textures/terrain/city/green_island_map.png";
    }

}
