package com.itique.ps.util.generate.setting;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import com.itique.ps.model.world.Terrain;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

public class AreaSettings {

    private int viewportWidth;
    private int viewportHeight;
    private Map<Terrain, Pixmap> terrainPixmaps;
    private int tileSize;
    private Pixmap cityMap;
    private int heroX;
    private int heroY;
    private Vector3 prevCameraPosition;
    private Vector3 newCameraPosition;

    public AreaSettings(int viewportWidth, int viewportHeight, Map<Terrain, Pixmap> terrainPixmaps, int tileSize,
                        Pixmap cityMap, int heroX, int heroY, Vector3 prevCameraPosition, Vector3 newCameraPosition) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.terrainPixmaps = terrainPixmaps;
        this.tileSize = tileSize;
        this.cityMap = cityMap;
        this.heroX = heroX;
        this.heroY = heroY;
        this.prevCameraPosition = prevCameraPosition;
        this.newCameraPosition = newCameraPosition;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(int viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(int viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public Map<Terrain, Pixmap> getTerrainPixmaps() {
        return terrainPixmaps;
    }

    public void setTerrainPixmaps(Map<Terrain, Pixmap> terrainPixmaps) {
        this.terrainPixmaps = terrainPixmaps;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public Pixmap getCityMap() {
        return cityMap;
    }

    public void setCityMap(Pixmap cityMap) {
        this.cityMap = cityMap;
    }

    public int getHeroX() {
        return heroX;
    }

    public void setHeroX(int heroX) {
        this.heroX = heroX;
    }

    public int getHeroY() {
        return heroY;
    }

    public void setHeroY(int heroY) {
        this.heroY = heroY;
    }

    public Vector3 getPrevCameraPosition() {
        return prevCameraPosition;
    }

    public void setPrevCameraPosition(Vector3 prevCameraPosition) {
        this.prevCameraPosition = prevCameraPosition;
    }

    public Vector3 getNewCameraPosition() {
        return newCameraPosition;
    }

    public void setNewCameraPosition(Vector3 newCameraPosition) {
        this.newCameraPosition = newCameraPosition;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("viewportWidth", viewportWidth)
                .append("viewportHeight", viewportHeight)
                .append("terrainPixmaps", terrainPixmaps)
                .append("tileSize", tileSize)
                .append("cityMap", cityMap)
                .append("heroX", heroX)
                .append("heroY", heroY)
                .append("prevCameraPosition", prevCameraPosition)
                .append("newCameraPosition", newCameraPosition)
                .toString();
    }

    public AreaSettings copy() {
        return new AreaSettings(this.viewportWidth, this.viewportHeight, this.terrainPixmaps,
                this.tileSize, this.cityMap, this.heroX, this.heroY,
                this.prevCameraPosition, this.newCameraPosition);
    }

}
