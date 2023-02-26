package com.itique.ps.service;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itique.ps.model.Man;

import java.util.List;
import java.util.Optional;

import static com.itique.ps.constant.Constant.*;

public class PersonFileDao implements FileDao<Man> {

    private final ObjectMapper objectMapper;

    public PersonFileDao() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(Man man) {
        try {
            List<Man> humans = findAll();
            humans.add(man);
            Gdx.files.local(HUMANS_DATA)
                    .writeString(objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(humans), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            Gdx.files.local(HUMANS_DATA)
                    .writeString(objectMapper.writeValueAsString(findAll().stream()
                            .filter(m -> !m.getId().equals(id))), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        Gdx.files.local(HUMANS_DATA).delete();
    }

    @Override
    public Optional<Man> findById(String id) {
        return findAll().stream().filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public Optional<Man> findHero() {
        return findAll().stream()
                .filter(m -> m.getId().equals(Gdx.app.getPreferences(GLOBAL_PREF)
                        .getString(HERO_ID_KEY)))
                .findFirst();
    }

    @Override
    public List<Man> findAll() {
        try {
            return objectMapper.readValue(Gdx.files.local(HUMANS_DATA)
                    .readString(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
