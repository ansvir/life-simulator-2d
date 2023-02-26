package com.itique.ps.service;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itique.ps.model.world.City;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.itique.ps.constant.Constant.CITIES_DATA;

public class CityFileDao implements FileDao<City> {

    private ObjectMapper objectMapper;

    public CityFileDao() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(City city) {
        try {
            List<City> cities = findAll();
            List<City> found = cities.stream()
                    .filter(c -> c.getId().equals(city.getId()))
                    .collect(Collectors.toList());
            if (found.isEmpty()) {
                cities.add(city);
            } else {
                int toRemoveIndex = 0;
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).getId().equals(city.getId())) {
                        toRemoveIndex = i;
                        break;
                    }
                }
                cities.remove(toRemoveIndex);
                cities.add(SerializationUtils.clone(found.get(0)));
            }
            Gdx.files.local(CITIES_DATA)
                    .writeString(objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(cities), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // for future release
//    @Override
//    public void save(City city) {
//        try {
//            CityMapFileDao cityMapDao = new CityMapFileDao();
//            List<City> cities = findAll();
//            List<City> found = cities.stream()
//                    .filter(c -> c.getId().equals(city.getId()))
//                    .collect(Collectors.toList());
//            Pixmap pixmap = generateCityMap(city);
//            CityMap map = new CityMap(city.getId(), pixmap);
//            if (found.isEmpty()) {
//                cities.add(city);
//            } else {
//                int toRemoveIndex = 0;
//                for (int i = 0; i < cities.size(); i++) {
//                    if (cities.get(i).getId().equals(city.getId())) {
//                        toRemoveIndex = i;
//                        break;
//                    }
//                }
//                cities.remove(toRemoveIndex);
//                cities.add(SerializationUtils.clone(found.get(0)));
//            }
//            cityMapDao.save(map);
//            pixmap.dispose();
//            Gdx.files.local(CITIES_DATA)
//                    .writeString(objectMapper.writerWithDefaultPrettyPrinter()
//                            .writeValueAsString(cities), false);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void deleteById(String id) {
        try {
            Gdx.files.local(CITIES_DATA)
                    .writeString(objectMapper.writeValueAsString(findAll().stream()
                            .filter(m -> !m.getId().equals(id))), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        Gdx.files.local(CITIES_DATA).delete();
    }

    @Override
    public Optional<City> findById(String id) {
        return findAll().stream().filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<City> findAll() {
        try {
            return objectMapper.readValue(Gdx.files.local(CITIES_DATA)
                    .readString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
