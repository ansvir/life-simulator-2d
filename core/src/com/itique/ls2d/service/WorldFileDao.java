package com.itique.ls2d.service;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itique.ls2d.model.world.World;

import java.util.List;
import java.util.Optional;

import static com.itique.ls2d.constant.Constant.WORLDS_DATA;

public class WorldFileDao implements FileDao<World> {

    private final ObjectMapper objectMapper;

    public WorldFileDao() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(World world) {
        try {
            List<World> worlds = findAll();
            worlds.add(world);
            Gdx.files.local(WORLDS_DATA)
                    .writeString(objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(worlds), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            Gdx.files.local(WORLDS_DATA)
                    .writeString(objectMapper.writeValueAsString(findAll().stream()
                            .filter(w -> !w.getId().equals(id))), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        Gdx.files.local(WORLDS_DATA).delete();
    }

    @Override
    public Optional<World> findById(String id) {
        return findAll().stream().filter(w -> w.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<World> findAll() {
        try {
            return objectMapper.readValue(Gdx.files.local(WORLDS_DATA)
                    .readString(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
