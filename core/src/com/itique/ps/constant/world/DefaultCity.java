package com.itique.ps.constant.world;

import com.itique.ps.model.world.City;
import com.itique.ps.model.world.Terrain;

import java.util.Map;

public interface DefaultCity {

    City generate();

    Map<Terrain, String> getTerrainTexturesPaths();

    String getMapPath();

}
