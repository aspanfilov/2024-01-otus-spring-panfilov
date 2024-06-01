package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(Set<String> ids);

    Optional<Genre> findById(String id);

    Genre insert(String name);

    Genre update(String id, String name);

    void deleteById(String id);
}
