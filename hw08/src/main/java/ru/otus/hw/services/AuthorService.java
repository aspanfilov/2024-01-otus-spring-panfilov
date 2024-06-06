package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Optional<Author> findById(String id);

    List<Author> findAll();

    Author insert(String fullName);

    Author update(String id, String name);

    void deleteById(String id);

    void deleteAll();
}
