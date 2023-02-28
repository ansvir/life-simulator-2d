package com.itique.ls2d.service.file;

import java.util.List;
import java.util.Optional;

public interface FileDao<T> {
    void save(T t);
    void deleteById(String id);
    void deleteAll();
    Optional<T> findById(String id);
    List<T> findAll();
}
