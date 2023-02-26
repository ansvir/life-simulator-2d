package com.itique.ls2d.constant.world;

import com.itique.ls2d.model.world.City;
import com.itique.ls2d.model.world.Terrain;

import java.util.Map;

public interface DefaultCity {

    City generate();

    Map<Terrain, String> getTerrainTexturesPaths();

    String getMapPath();

}
