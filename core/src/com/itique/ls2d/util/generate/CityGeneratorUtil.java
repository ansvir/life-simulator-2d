package com.itique.ls2d.util.generate;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.itique.ls2d.model.world.City;
import com.itique.ls2d.util.generate.setting.AreaSettings;

import java.util.List;

import static com.itique.ls2d.util.ResourceUtil.resizePixmap;
import static java.lang.Math.random;

public class CityGeneratorUtil {

    @Deprecated(since = "Will be used in future releases")
    public static City generateCity() {
        NameGeneratorUtil.Config nameConfig = NameGeneratorUtil.Config
                .builder().capitalize().build();
        float squareKm = (float) (random() * 1000f + 50f);
        int population = (int) (random() * 2000000 + 10);
        return new City(
                NameGeneratorUtil.generateName(12, nameConfig),
                squareKm, population, List.of());
    }

    public static City generateCity(String defaultId, String name, float squareKm, int population) {
        return new City(defaultId, name, squareKm, population, List.of());
    }

    @Deprecated(since = "Will be used in future releases")
    public static Pixmap generateCityMap(City city) {
        float skm = city.getSquareKm();
        Pixmap pixmap = new Pixmap((int) skm, (int) skm, Pixmap.Format.RGBA8888);
        Pixmap grassPixmap = TerrainGeneratorUtil.getGrassPixmap();
        for (int x = 0; x < skm; x += 1) {
            for (int y = 0; y < skm; y += 1) {
                pixmap.drawPixmap(grassPixmap, x, y, 0, 0, grassPixmap.getWidth(), grassPixmap.getHeight());
            }
        }
        grassPixmap.dispose();
        return pixmap;
    }

    public static Texture generateCityArea(AreaSettings settings) {
        Pixmap area = new Pixmap(settings.getViewportWidth(), settings.getViewportHeight(), Pixmap.Format.RGBA8888);
        int mapWidth = settings.getCityMap().getWidth();
        int mapHeight = settings.getCityMap().getHeight();
        int heroX = settings.getHeroX() / settings.getTileSize();
        int heroY = settings.getHeroY() / settings.getTileSize();
        int tilesWidth = settings.getViewportWidth() / settings.getTileSize();
        int tilesHeight = settings.getViewportHeight() / settings.getTileSize();
        int xStart = mapWidth - heroX - tilesWidth / 2;
        int yStart = mapHeight - heroY - tilesHeight / 2;
        int xEnd = xStart + tilesWidth;
        int yEnd = yStart + tilesHeight;
        int areaX = 0;
        int areaY = 0;
        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                Pixmap tile = settings.getTerrainPixmaps()
                        .get(TerrainGeneratorUtil.getTerrainFromPixel(settings.getCityMap().getPixel(x, y)));
                tile = resizePixmap(tile, settings.getTileSize(), settings.getTileSize());
                area.drawPixmap(tile, areaX * settings.getTileSize(),
                        areaY * settings.getTileSize());
                areaY++;
                tile.dispose();
            }
            areaX++;
            areaY = 0;
        }
        Texture areaTexture = new Texture(area);
        area.dispose();
        return areaTexture;
    }

}
