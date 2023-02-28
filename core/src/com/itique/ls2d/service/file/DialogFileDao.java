package com.itique.ls2d.service.file;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itique.ls2d.model.dialog.Dialog;

import java.util.List;
import java.util.Optional;

import static com.itique.ls2d.constant.Constant.DIALOG_DATA;

public class DialogFileDao implements FileDao<Dialog> {

    private ObjectMapper objectMapper;

    public DialogFileDao() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(Dialog dialog) {
        try {
            List<Dialog> dialogs = findAll();
            dialogs.add(dialog);
            Gdx.files.local(DIALOG_DATA)
                    .writeString(objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(dialogs), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            Gdx.files.local(DIALOG_DATA)
                    .writeString(objectMapper.writeValueAsString(findAll().stream()
                            .filter(d -> !d.getId().equals(id))), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        Gdx.files.local(DIALOG_DATA).delete();
    }

    @Override
    public Optional<Dialog> findById(String id) {
        return findAll().stream().filter(d -> d.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Dialog> findAll() {
        try {
            return objectMapper.readValue(Gdx.files.local(DIALOG_DATA)
                    .readString(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
