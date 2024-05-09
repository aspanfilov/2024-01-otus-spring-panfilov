package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreService {
    List<Genre> findAll();

    List<Genre> findAllByIds(Set<Long> ids);

    Optional<Genre> findById(long id);

    Genre insert(String name);

    Genre update(long id, String name);

    void deleteById(long id);
}
