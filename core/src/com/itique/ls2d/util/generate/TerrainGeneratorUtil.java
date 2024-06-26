package com.itique.ls2d.util.generate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.itique.ls2d.constant.RGBAConstant;
import com.itique.ls2d.model.world.Terrain;

public class TerrainGeneratorUtil {

    public static Pixmap getGrassPixmap() {
        Texture grass = new Texture(Gdx.files.internal("textures/terrain/grass.png"));
        grass.getTextureData().prepare();
        return grass.getTextureData().consumePixmap();
    }

    public static Terrain getTerrainFromPixel(int rgba8888) {
        switch (rgba8888) {
            case RGBAConstant.SAND:
                return Terrain.SAND;
            case RGBAConstant.ROCK:
                return Terrain.ROCK;
            case RGBAConstant.ROAD:
                return Terrain.ROAD;
            case RGBAConstant.TREE:
                return Terrain.TREE;
            case RGBAConstant.BUILDING:
                return Terrain.BUILDING;
            case RGBAConstant.WATER:
                return Terrain.WATER;
            case RGBAConstant.HILL_1:
                return Terrain.HILL_1;
            case RGBAConstant.HILL_2:
                return Terrain.HILL_2;
            case RGBAConstant.HILL_3:
                return Terrain.HILL_3;
            case RGBAConstant.HILL_4:
                return Terrain.HILL_4;
            case RGBAConstant.HILL_5:
                return Terrain.HILL_5;
            case RGBAConstant.HILL_6:
                return Terrain.HILL_6;
            default:
                return Terrain.GRASS;
        }
    }
}
