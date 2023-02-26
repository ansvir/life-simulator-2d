package com.itique.ls2d.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.itique.ls2d.model.CityMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Deprecated(since = "Will be used in future releases")
public class CityMapFileDao implements FileDao<CityMap> {

    private static final String CITIES_MAPS_PREFIX = "data/map/city/";
    private static final String CITIES_MAPS_POSTFIX = ".cim";

    @Override
    public void save(CityMap cityMap) {
        FileHandle image = Gdx.files.local(CITIES_MAPS_PREFIX + cityMap.getCityId() + CITIES_MAPS_POSTFIX);
        PixmapIO.writeCIM(image, cityMap.getImage());
    }

    @Override
    public void deleteById(String cityId) {
        Gdx.files.local(CITIES_MAPS_PREFIX + cityId + CITIES_MAPS_POSTFIX).delete();
    }

    @Override
    public void deleteAll() {
        Gdx.files.local(CITIES_MAPS_PREFIX).deleteDirectory();
    }

    @Override
    public Optional<CityMap> findById(String cityId) {
        FileHandle file = Gdx.files.local(CITIES_MAPS_PREFIX + cityId + CITIES_MAPS_POSTFIX);
        if (file == null) {
            return Optional.empty();
        } else {
            Pixmap image = PixmapIO.readCIM(file);
            return Optional.of(new CityMap(cityId, image));
        }
    }

    @Override
    public List<CityMap> findAll() {
        return Arrays.stream(Gdx.files.local(CITIES_MAPS_PREFIX).list())
                .map(f -> new CityMap(f.file().getName(), PixmapIO.readCIM(f)))
                .collect(Collectors.toList());
    }

}
